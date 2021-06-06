/*
 * SaleReportDocument.java
 *
 * Created on February 19, 2005, 10:53 AM
 */

package com.dimata.posbo.report.sale;

import java.util.*;
import java.util.Date;
import java.sql.*;

import com.dimata.util.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.location.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.payment.PstCashPayment;
import com.dimata.posbo.entity.search.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.DBHandler;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author wpradnyana
 */
public class SaleReportDocument {

    public static String SALE_REPORT_DOC = "SALE_REPORT_DOC";
    public static String SALE_REPORT_DOC_PDF = "SALE_REPORT_DOC_PDF";
    public static String SALE_REPORT_DOC_DETAIL = "SALE_REPORT_DOC_DETAIL";
    public static String SALE_REPORT_DOC_DETAIL_PDF = "SALE_REPORT_DOC_DETAIL_PDF";
    public static String SALE_REPORT_DOC_MARGIN_PDF = "SALE_REPORT_DOC_MARGIN_PDF";
    public static String AR_REPORT_DOC = "AR_REPORT_DOC";
    public static String AR_PAYMENT_DOC = "AR_PAYMENT_DOC";
    public static String AR_INVOICE_PDF = "AR_INVOICE_PDF";
    public static int LOG_MODE_NONE = 0;
    public static int LOG_MODE_FILE = 1;
    public static int LOG_MODE_CONSOLE = 2;
    Vector saleReportItem;
    
    public String NAME_HOST_REMOTE = "27.0.0.1";
    public String TBL_BILL_MAIN_HOST_REMOTE = "";

    /**
     * GADNYANA
     * PROCESS FOR sort by THE FIELD COLOUM
     */
/*    public static int SORT_FIELD_CODE = 0;
    public static int SORT_FIELD_NAME = 1;
    public static int SORT_FIELD_CATEGORY = 2;
    public static int SORT_FIELD_MERK = 3;
    public static int SORT_FIELD_SELL_PRICE = 4;
    public static int SORT_FIELD_TOTAL_SALE = 5;
    public static int SORT_FIELD_QTY = 6;
    public static int SORT_FIELD_SUPPLIER = 7;
    public static int SORT_FIELD_BUYING_PRICE = 8;
    public static int SORT_FIELD_SALES_NAME = 9;
    public static int SORT_FIELD_LOCATION = 10;
    public static int SORT_FIELD_SHIFT = 11;

    public static String[] sortFieldColoum = {"Code", "Name", "Category", "Merk", "Selling Price",
            "Total Sale", "Quantity", "Supplier", "Buying Price", "Sales",
            "Location", "Shift"};*/

    /**
     * Creates a new instance of SaleReportDocument
     */
    public SaleReportDocument() {
	
    }

    public SaleReportDocument(String nameHost) {
	NAME_HOST_REMOTE = nameHost;
	NAME_HOST_REMOTE = NAME_HOST_REMOTE.replaceAll(".", "_");
	TBL_BILL_MAIN_HOST_REMOTE = PstBillMain.TBL_CASH_BILL_MAIN +"_"+NAME_HOST_REMOTE;
    }
    /**
     * Getter for property saleReportItem.
     *
     * @return Value of property saleReportItem.
     */
    public java.util.Vector getSaleReportItem() {
        return saleReportItem;
    }

    /**
     * Setter for property saleReportItem.
     *
     * @param saleReportItem New value of property saleReportItem.
     */
    public void setSaleReportItem(java.util.Vector saleReportItem) {
        this.saleReportItem = saleReportItem;
    }

    
    public void createTblRequestSale(){
	try{
	    if(NAME_HOST_REMOTE!=null && NAME_HOST_REMOTE.length()>0){
		
		String tbl_host_request = "CREATE TABLE `"+TBL_BILL_MAIN_HOST_REMOTE+"` ( " +
		  "`NOTES` varchar(100) default '',"+
		  "`PARENT_ID` bigint(20) default '0',"+
		  "`CASH_BILL_MAIN_ID` bigint(20) NOT NULL default '0',"+
		  "`CASH_CASHIER_ID` bigint(20) NOT NULL default '0',"+
		  "`LOCATION_ID` bigint(20) NOT NULL default '0',"+
		  "`CASH_PENDING_ORDER_ID` bigint(20) NOT NULL default '0',"+
		  "`BILL_DATE` datetime NOT NULL default '0000-00-00 00:00:00',"+
		  "`BILL_NUMBER` varchar(20) default NULL,"+
		  "`APP_USER_ID` bigint(20) default NULL,"+
		  "`SHIFT_ID` bigint(20) default NULL,"+
		  "`DISC` double default NULL,"+
		  "`TAX_PCT` double default NULL,"+
		  "`TAX_VALUE` double default NULL,"+
		  "`SERVICE_PCT` double default NULL,"+
		  "`SERVICE_VALUE` double default NULL,"+
		  "`DISC_TYPE` int(11) default NULL,"+
		  "`BILL_STATUS` int(11) default NULL,"+
		  "`SALES_CODE` varchar(20) default NULL,"+
		  "`GUEST_NAME` varchar(100) default '',"+
		  "`INVOICE_COUNTER` int(11) default NULL,"+
		  "`INVOICE_NUMBER` varchar(10) default NULL,"+
		  "`DOC_TYPE` int(11) NOT NULL default '0',"+
		  "`TRANS_TYPE` int(4) NOT NULL default '0',"+
		  "`CUSTOMER_ID` bigint(20) default NULL,"+
		  "`TRANSACTION_TYPE` tinyint(4) default NULL,"+
		  "`TRANSACTION_STATUS` int(4) default NULL,"+
		  "`COVER_NUMBER` varchar(20) default NULL,"+
		  "`SPECIAL_ID` bigint(20) default NULL,"+
		  "`CURRENCY_ID` bigint(20) NOT NULL default '0',"+
		  "`RATE` double NOT NULL default '0',"+
		  "`RESERVATION_ID` bigint(20) NOT NULL default '0',"+
		  "`SPECIAL_FLAG` int(4) default NULL,"+
		  "`DISC_AMOUNT` double default NULL,"+
		  "`VESSEL` varchar(255) default NULL,"+
		  "`SAIL_ON_ABOUT` datetime default NULL,"+
		  "`PORT_OF_LOADING` varchar(255) default NULL,"+
		  "`PORT_OF_DESTINATION` varchar(255) default NULL,"+
		  "`CONTAINER_NUMBER` varchar(255) default NULL,"+
		  "`SEAL_NUMBER` varchar(255) default NULL,"+
		  "`LOADING_DATE` datetime default NULL,"+
		  "`SHIPPING_DATE` datetime default NULL,"+
		  "`STOCK_LOCATION_ID` bigint(20) default NULL,"+
		  "PRIMARY KEY  (`CASH_BILL_MAIN_ID`)"+
		") ENGINE=MyISAM DEFAULT CHARSET=latin1;";
		
		DBHandler.execUpdate(tbl_host_request);
	    }
	}catch(Exception e){}
    }
    

    /**
     * @param srcSaleReport
     * @param start
     * @param recordToGet
     * @param logMode
     */
    public static Vector getList(SrcSaleReport srcSaleReport, int start, int recordToGet, int logMode) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            Shift shift = null;
            String stTime = "";
            String edTime = "";
            Date dtTo = new Date();
           /** if (srcSaleReport.getShiftId() > 0) {
                shift = PstShift.fetchExc(srcSaleReport.getShiftId());
                stTime = Formater.formatDate(shift.getStartTime(), "HH:mm:00");
                edTime = Formater.formatDate(shift.getEndTime(), "HH:mm:00");
                dtTo = new Date(srcSaleReport.getDateFrom().getTime());
                if (shift.getStartTime().getHours() > shift.getEndTime().getHours()) {
                    dtTo.setDate(dtTo.getDate() + 1);
                    //srcSaleReport.setDateTo(dtTo);
                } else {
                    //srcSaleReport.setDateTo(dtTo);
                }
            } else {**/
                stTime = "00:00:00";
                edTime = "23:59:59";
           // }
            //update opie-eyek 20171128
            
            String sql = "";
            switch (srcSaleReport.getQueryType()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    sql = "SELECT cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "," +
                            " cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_category as cat" +
                            " on cat.category_id =mat.category_id)";
                    break;

                case SrcSaleReport.GROUP_BY_LOCATION:
                    sql = sql + "SELECT mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "," +
                            " loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            
                           //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            " FROM ";
                    sql = sql + "cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id" +
                            //" inner join location as loc" +
                            //" on loc.location_id = cbm.location_id" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id";
                    if (srcSaleReport.getCategoryId() != 0) {
                        sql = sql + " inner join pos_category as cat" +
                                " on cat.category_id = mat.category_id";
                    }
                    if (srcSaleReport.getMarkId() != 0) {
                        sql = sql + " inner join pos_merk as merk" +
                                " on merk.merk_id = mat.merk_id";
                    }
                    System.out.println("-->> GROUP_BY_LOCATION : ");
                    break;		    

                case SrcSaleReport.GROUP_BY_SALES:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "," +
                            " sps." + PstSales.fieldNames[PstSales.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty * cbm.rate) as cbd_cost," +
                            
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            
                            " count(distinct(cbd.cash_bill_main_id)) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join " +
                            " pos_sales_person as sps" +
                            " on sps.sales_code = cbm.sales_code)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_SHIFT:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "," +
                            " shift." + PstShift.fieldNames[PstShift.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", COUNT(DISTINCT cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+") as tot_transaksi"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join " +
                            " pos_shift as shift" +
                            " on cbm.shift_id = shift.shift_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "," +
                            " sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            
//                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
//                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
//                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
//                            
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            " FROM ((((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            /*" inner join pos_vendor_price as vpc" +
                            " on vpc.material_id= mat.material_id" hide opie-eyek pakai di supplier master**/
                            ")" +
                            " left join contact_list as sup" +
                            " on mat.supplier_id = sup.contact_id)";
                    break;

                case SrcSaleReport.GROUP_BY_DATE:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
//                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
//                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
//                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            " FROM ((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_ITEM:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
//                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
//                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
//                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_category as cat" +
                            " on cat.category_id =mat.category_id)";
                    break;

                case SrcSaleReport.GROUP_BY_MARK:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "," +
                            " merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
//                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
//                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
//                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_merk as merk" +
                            " on merk.merk_id = mat.merk_id)";
                    break;
            }
            
            /** melakukan inner ke table lokasi, karena setiap transaksi pasti memiliki lokasi transaksi (gwawan@dimata 20080114)*/
            sql += " inner join location as loc" + " on loc.location_id = cbm.location_id";
            
            // inner join ke table pos_unit
            //sql+= " inner join "+PstUnit.TBL_P2_UNIT+" unit on cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID];
            //sql+= " = unit."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

            // ini untuk penambahan query jika pencarian secara global
            /*if (srcSaleReport.getQueryType() == srcSaleReport.getGroupBy()) {
                if (srcSaleReport.getQueryType() != SrcSaleReport.GROUP_BY_LOCATION) {
                    sql = sql + " inner join location as loc" +
                            " on loc.location_id = cbm.location_id";
                }
            }
             */
            
            String dateTrans = "";
            if (srcSaleReport.getDateFrom() != null) {
                String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                dateTrans = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
            }

            String itemId = "";
            if (srcSaleReport.getItemId() > 0) {
                itemId = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "=" + srcSaleReport.getItemId();
            }

            String itemName = "";
            if (srcSaleReport.getItemName().length() > 0) {
                itemName = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "=" + srcSaleReport.getItemName();
            }

            String locationId = "";
            if (srcSaleReport.getLocationId() != 0) {
                locationId = " loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
            }
            
            String categoryId = "";
            String subCategoryId = "";
            String salesPersonId = "";
            String shiftId = "";
            String supplierId = "";
            String markId = "";
            
            /** kondisi berdasarkan jenis group yang dipilih */
            switch (srcSaleReport.getQueryType()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    if (srcSaleReport.getCategoryId() != 0) {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + srcSaleReport.getCategoryId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUB_CATEGORY:
                    if (srcSaleReport.getSubCategoryId() != 0) {
                        subCategoryId = " scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + "=" + srcSaleReport.getSubCategoryId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    if (srcSaleReport.getSalesPersonId() != 0) {
                        salesPersonId = " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "=" + srcSaleReport.getSalesPersonId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    if (srcSaleReport.getShiftId() != 0) {
                        shiftId = " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    if (srcSaleReport.getSupplierId() != 0) {
                        supplierId = " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "=" + srcSaleReport.getSupplierId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK:
                    if (srcSaleReport.getMarkId() != 0) {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + srcSaleReport.getMarkId();
                    }
                    break;
                    
                default:
                    break;
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListCategoryId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListCategoryId().size(); k++) {
                    long oidCateg = Long.parseLong((String) srcSaleReport.getListCategoryId().get(k));
                    if (categoryId.length() > 0) {
                        categoryId = categoryId + " or cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    } else {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    }
                }
                categoryId = "(" + categoryId + ")";
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListMerkId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListMerkId().size(); k++) {
                    long oidMerk = Long.parseLong((String) srcSaleReport.getListMerkId().get(k));
                    if (markId.length() > 0) {
                        markId = markId + " or merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    } else {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    }
                }
                markId = "(" + markId + ")";
            }
            
            String transType = "";
            //if (srcSaleReport.getTransType() >= 0) {
            if (srcSaleReport.getTransType() >= 0 && srcSaleReport.getTransType()<6) {
                int[] transMode = getTransMode(srcSaleReport);
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] +
                           // " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE];
                             //add by Mirah
                              " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] +
                              " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS];
                
                            //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS] +
                            //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_TYPE]+
            }

            //if trans type cash credit
            else if (srcSaleReport.getTransType()== 6){
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")" ;
            }

            
            /*
            if(transType.length()>0)
                transType = transType + " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
            else
                transType = transType + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
             */
            
            //String strBillStatus = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            
            String whereClause = "";
            if (locationId.length() > 0)
                whereClause = locationId;
            
            if (categoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + categoryId;
                } else {
                    whereClause = whereClause + categoryId;
                }
            }
            
            if (dateTrans.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + dateTrans;
                } else {
                    whereClause = whereClause + dateTrans;
                }
            }
            
            if (itemName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemName;
                } else {
                    whereClause = whereClause + itemName;
                }
            }
            
            if (itemId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemId;
                } else {
                    whereClause = whereClause + itemId;
                }
            }
            
            if (locationId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + locationId;
                } else {
                    whereClause = whereClause + locationId;
                }
            }
            
            if (markId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + markId;
                } else {
                    whereClause = whereClause + markId;
                }
            }
            
            if (salesPersonId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesPersonId;
                } else {
                    whereClause = whereClause + salesPersonId;
                }
            }
            
            if (shiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + shiftId;
                } else {
                    whereClause = whereClause + shiftId;
                }
            }
            
            if (subCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + subCategoryId;
                } else {
                    whereClause = whereClause + subCategoryId;
                }
            }
            
            if (supplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + supplierId;
                } else {
                    whereClause = whereClause + supplierId;
                }
            }
            
            if (transType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transType;
                } else {
                    whereClause = whereClause + transType;
                }
            }
            
            //if (strBillStatus.length() > 0) {
                //if (whereClause.length() > 0) {
                   // whereClause = whereClause + " AND " + strBillStatus;
                //} else {
                    //whereClause = whereClause + strBillStatus;
               // }
           // }
            
            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause; //+" AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            } else {
                sql = sql + " where cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }
            
            /* hide dulu opie-eyek 20160806*/
            switch (srcSaleReport.getGroupBy()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    sql = sql + "\n  group by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_LOCATION:
                    sql = sql + "\n  group by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    sql = sql + "\n  group by sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    sql = sql + "\n  group by shift." + PstShift.fieldNames[PstShift.FLD_START_TIME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    sql = sql + "\n  group by sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_ITEM:
                    sql = sql + "\n  group by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
                    
                case SrcSaleReport.GROUP_BY_DATE:
                    sql = sql + "\n  group by mat." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK: 
                    sql = sql + "\n  group by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                    break;
                    
                default:
                    sql = sql + "\n  group by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
            }
            
            String childGroup = "";
            switch (srcSaleReport.getDetailGroup()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    childGroup = "\n  cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_LOCATION:
                    childGroup = "\n  loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    childGroup = "\n  sps." + PstSales.fieldNames[PstSales.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    childGroup = "\n  shift." + PstShift.fieldNames[PstShift.FLD_START_TIME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    childGroup = "\n  sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_ITEM:
                    childGroup = "\n  mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_DATE:
                    childGroup = "\n  cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK:
                    childGroup = "\n  merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                    break;
                    
                default:
                    childGroup = "\n  mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
            }
            
            String descSort = "";
            if (srcSaleReport.getDescSort() == SrcSaleReport.SORT_DESC) {
                descSort = " DESC ";
            } else {
                descSort = " ASC ";
            }
            
            switch (srcSaleReport.getSortBy()) {
                case SrcSaleReport.SORT_BY_DATE:
                    sql = sql + "\n  order by cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_LOCATION:
                    sql = sql + "\n  order by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARK:
                    sql = sql + "\n  order by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SALES_PERSON:
                    sql = sql + "\n  order by sps." + PstSales.fieldNames[PstSales.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SHIFT:
                    sql = sql + "\n  order by shift." + PstShift.fieldNames[PstShift.FLD_START_TIME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SUB_CATEGORY:
                    sql = sql + "\n  order by scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_CATEGORY:
                    sql = sql + "\n  order by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SUPPLIER:
                    sql = sql + "\n  order by sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_ITEM:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_TOTAL_QTY:
                    sql = sql + "\n  order by cbd_qty " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_TOTAL_SALE:
                    sql = sql + "\n  order by cbd_total " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARGIN:
                    sql = sql + "\n  order by cbd_margin " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARGIN_PCT:
                    sql = sql + "\n  order by cbd_margin_pct " + descSort + " ";
                    break;
                    
                default:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
            }
            
            if(recordToGet!=0){
                sql = sql + "\n  limit 0,"+recordToGet+"";
            }
            
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //Vector temp = new Vector(1,1);
                SaleReportItem item = new SaleReportItem();
                //Unit unit = new Unit();
                if (srcSaleReport.getQueryType()== SrcSaleReport.GROUP_BY_CATEGORY) {
                    item.setCategoryId(rs.getLong("cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ""));
                    item.setCategoryName(rs.getString("cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + ""));
                }
                
                item.setItemId(rs.getLong("mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ""));
                item.setItemCode(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ""));
                item.setItemName(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ""));
                item.setItemBarcode(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + ""));
                String matName = "";
                try {
                    Material mats = PstMaterial.fetchExc(item.getItemId());
                    matName = mats.getName();
                    item.setItemName(matName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setItemQty(rs.getDouble("cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ""));
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_LOCATION) {
                    item.setLocationId(rs.getLong("loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ""));
                    item.setLocationName(rs.getString("loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_MARK) {
                    item.setMarkId(rs.getLong("merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + ""));
                    item.setMarkName(rs.getString("merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SALES) {
                    item.setSalesPersonId(rs.getLong("sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + ""));
                    item.setSalesPersonName(rs.getString("sps." + PstSales.fieldNames[PstSales.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                    item.setShiftId(rs.getLong("shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ""));
                    item.setShiftName(rs.getString("shift." + PstShift.fieldNames[PstShift.FLD_NAME] + ""));
                    item.setTotTransaksi(rs.getInt("tot_transaksi"));
                }else{
                    if(srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                        try{
                            item.setShiftId(rs.getLong("shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ""));
                            item.setShiftName(rs.getString("shift." + PstShift.fieldNames[PstShift.FLD_NAME] + ""));
                            item.setTotTransaksi(rs.getInt("tot_transaksi"));
                        }catch(Exception es){
                        }
                    }
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SUB_CATEGORY) {
                    item.setSubCategoryId(rs.getLong("scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + ""));
                    item.setSubCategoryName(rs.getString("scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                    item.setSupplierId(rs.getLong("sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + ""));
                    item.setSupplierName(rs.getString("sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + ""));
                }
                
                item.setDiscount(rs.getDouble("cbd_disc"));
                item.setTotalQty(rs.getDouble("cbd_qty"));
                item.setTotalSold(rs.getDouble("cbd_total"));
                item.setTotalCost(rs.getDouble("cbd_cost"));
                item.setTotalMargin(rs.getDouble("cbd_margin"));
                item.setTotalMarginPct(rs.getDouble("cbd_margin_pct"));
                item.setTotalQtyByStock(rs.getDouble("cbd_qty_stock"));
                item.setTotalSoldByStock(rs.getDouble("cbd_item_price_stock"));
                //temp.add(item);
                
                //unit.setCode(rs.getString("unit."+PstUnit.fieldNames[PstUnit.FLD_CODE]));
                //temp.add(unit);
                
                result.add(item);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Vector getListJoinWithMasterTypeForPromosi(SrcSaleReport srcSaleReport, int start, int recordToGet, int logMode) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            Shift shift = null;
            String stTime = "";
            String edTime = "";
            Date dtTo = new Date();
           /** if (srcSaleReport.getShiftId() > 0) {
                shift = PstShift.fetchExc(srcSaleReport.getShiftId());
                stTime = Formater.formatDate(shift.getStartTime(), "HH:mm:00");
                edTime = Formater.formatDate(shift.getEndTime(), "HH:mm:00");
                dtTo = new Date(srcSaleReport.getDateFrom().getTime());
                if (shift.getStartTime().getHours() > shift.getEndTime().getHours()) {
                    dtTo.setDate(dtTo.getDate() + 1);
                    //srcSaleReport.setDateTo(dtTo);
                } else {
                    //srcSaleReport.setDateTo(dtTo);
                }
            } else {**/
                stTime = "00:00:00";
                edTime = "23:59:59";
           // }
            //update opie-eyek 20171128
            
            String sql = "";
            switch (srcSaleReport.getQueryType()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    sql = "SELECT cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "," +
                            " cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            //added by dewok 20181207 for greenbowl sales report
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "" +
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "" +
                            ", cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + "" +
                            ", mt.MASTER_NAME AS PROMOSI " +
                            ", mt.MASTER_TYPE_ID " +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_category as cat" +
                            " on cat.category_id =mat.category_id)";
                    break;

                case SrcSaleReport.GROUP_BY_LOCATION:
                    sql = sql + "SELECT mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "," +
                            " loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            
                           //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            //added by dewok 20181207 for greenbowl sales report
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "" +
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "" +
                            ", cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + "" +
                            ", mt.MASTER_NAME AS PROMOSI " +
                            ", mt.MASTER_TYPE_ID " +
                            " FROM ";
                    sql = sql + "cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id" +
                            //" inner join location as loc" +
                            //" on loc.location_id = cbm.location_id" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id";
                    if (srcSaleReport.getCategoryId() != 0) {
                        sql = sql + " inner join pos_category as cat" +
                                " on cat.category_id = mat.category_id";
                    }
                    if (srcSaleReport.getMarkId() != 0) {
                        sql = sql + " inner join pos_merk as merk" +
                                " on merk.merk_id = mat.merk_id";
                    }
                    System.out.println("-->> GROUP_BY_LOCATION : ");
                    break;		    

                case SrcSaleReport.GROUP_BY_SALES:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "," +
                            " sps." + PstSales.fieldNames[PstSales.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty * cbm.rate) as cbd_cost," +
                            
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            
                            " count(distinct(cbd.cash_bill_main_id)) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            //added by dewok 20181207 for greenbowl sales report
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "" +
                            ", cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + "" +
                            ", mt.MASTER_NAME AS PROMOSI " +
                            ", mt.MASTER_TYPE_ID " +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join " +
                            " pos_sales_person as sps" +
                            " on sps.sales_code = cbm.sales_code)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_SHIFT:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "," +
                            " shift." + PstShift.fieldNames[PstShift.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", COUNT(DISTINCT cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+") as tot_transaksi"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            //added by dewok 20181207 for greenbowl sales report
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "" +
                            ", cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + "" +
                            ", mt.MASTER_NAME AS PROMOSI " +
                            ", mt.MASTER_TYPE_ID " +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join " +
                            " pos_shift as shift" +
                            " on cbm.shift_id = shift.shift_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    sql = "SELECT " +
                            //" cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
							"IF(cbm.DOC_TYPE = 1 AND cbmp.BILL_DATE IS NOT NULL,cbmp.BILL_DATE, cbm.BILL_DATE) AS 'cbm.BILL_DATE',"+
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " IF(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] +" = " + PstBillMain.TYPE_RETUR+  ", '-1',  sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + ") AS supplierId,"+
                            " IF(cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] +" = " + PstBillMain.TYPE_RETUR+  ", 'RETURN',  sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + ") AS supplier,"+
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            
//                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
//                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
//                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
//                            
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            //added by dewok 20181207 for greenbowl sales report
                            //", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "" +
							", IF(cbm.DOC_TYPE = 1 AND cbmp.INVOICE_NUMBER IS NOT NULL,cbmp.INVOICE_NUMBER, cbm.INVOICE_NUMBER) AS 'cbm.INVOICE_NUMBER'"+
                            ", cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + "" +
                            ", mt.MASTER_NAME AS PROMOSI " +
                            ", mt.MASTER_TYPE_ID " +
                            " FROM ((((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id " +
							" LEFT JOIN cash_bill_main AS cbmp " +
							" ON cbmp.CASH_BILL_MAIN_ID = cbm.PARENT_ID )" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            /*" inner join pos_vendor_price as vpc" +
                            " on vpc.material_id= mat.material_id" hide opie-eyek pakai di supplier master**/
                            ")" +
                            " left join contact_list as sup" +
                            " on mat.supplier_id = sup.contact_id)";
                    break;

                case SrcSaleReport.GROUP_BY_DATE:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
//                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
//                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
//                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            //added by dewok 20181207 for greenbowl sales report
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "" +
                            ", cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + "" +
                            ", mt.MASTER_NAME AS PROMOSI " +
                            ", mt.MASTER_TYPE_ID " +
                            " FROM ((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_ITEM:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
//                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
//                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
//                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            //added by dewok 20181207 for greenbowl sales report
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "" +
                            ", cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + "" +
                            ", mt.MASTER_NAME AS PROMOSI " +
                            ", mt.MASTER_TYPE_ID " +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_category as cat" +
                            " on cat.category_id =mat.category_id)";
                    break;

                case SrcSaleReport.GROUP_BY_MARK:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "," +
                            " merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
//                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
//                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
//                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            //" sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate), ((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) AS cbd_total, "+
                            //" (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty) AS cbd_margin, "+
                            //" ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " ((sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate))) - SUM(cbd.cost * cbd.qty))/sum(if(cbd.total_price=(cbm.amount+cbm.disc),((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty),((cbd.total_price - (((cbm.disc / (cbm.amount)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty))) * 100 ) AS cbd_margin_pct, "+
                            
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + "" +
                            //added by dewok 20181207 for greenbowl sales report
                            ", cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + "" +
                            ", cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE] + "" +
                            ", mt.MASTER_NAME AS PROMOSI " +
                            ", mt.MASTER_TYPE_ID " +
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id " +
							" LEFT JOIN cash_bill_main AS cbmp " +
							" ON cbmp.CASH_BILL_MAIN_ID = cbm.PARENT_ID )" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_merk as merk" +
                            " on merk.merk_id = mat.merk_id)";
                    break;
            }
            
            /** melakukan inner ke table lokasi, karena setiap transaksi pasti memiliki lokasi transaksi (gwawan@dimata 20080114)*/
            sql += " inner join location as loc" + " on loc.location_id = cbm.location_id";
            
            sql += ""
                    + " LEFT JOIN data_custom AS dc "
                    + "    ON dc.owner_id = cbd.CASH_BILL_DETAIL_ID "
                    + " LEFT JOIN master_type AS mt "
                    + "    ON mt.MASTER_TYPE_ID = dc.data_value "
                    + "    AND mt.TYPE_GROUP = '5'";

            // inner join ke table pos_unit
            //sql+= " inner join "+PstUnit.TBL_P2_UNIT+" unit on cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_UNIT_ID];
            //sql+= " = unit."+PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

            // ini untuk penambahan query jika pencarian secara global
            /*if (srcSaleReport.getQueryType() == srcSaleReport.getGroupBy()) {
                if (srcSaleReport.getQueryType() != SrcSaleReport.GROUP_BY_LOCATION) {
                    sql = sql + " inner join location as loc" +
                            " on loc.location_id = cbm.location_id";
                }
            }
             */
            
            String dateTrans = "";
            if (srcSaleReport.getDateFrom() != null) {
                String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                dateTrans = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
            }

            String itemId = "";
            if (srcSaleReport.getItemId() > 0) {
                itemId = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "=" + srcSaleReport.getItemId();
            }

            String itemName = "";
            if (srcSaleReport.getItemName().length() > 0) {
                itemName = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "=" + srcSaleReport.getItemName();
            }

            String locationId = "";
            if (srcSaleReport.getLocationId() != 0) {
                locationId = " loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
            }
            
            String categoryId = "";
            String subCategoryId = "";
            String salesPersonId = "";
            String shiftId = "";
            String supplierId = "";
            String markId = "";
            
            /** kondisi berdasarkan jenis group yang dipilih */
            switch (srcSaleReport.getQueryType()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    if (srcSaleReport.getCategoryId() != 0) {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + srcSaleReport.getCategoryId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUB_CATEGORY:
                    if (srcSaleReport.getSubCategoryId() != 0) {
                        subCategoryId = " scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + "=" + srcSaleReport.getSubCategoryId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    if (srcSaleReport.getSalesPersonId() != 0) {
                        salesPersonId = " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "=" + srcSaleReport.getSalesPersonId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    if (srcSaleReport.getShiftId() != 0) {
                        shiftId = " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    if (srcSaleReport.getSupplierId() != 0) {
                        supplierId = " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "=" + srcSaleReport.getSupplierId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK:
                    if (srcSaleReport.getMarkId() != 0) {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + srcSaleReport.getMarkId();
                    }
                    break;
                    
                default:
                    break;
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListCategoryId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListCategoryId().size(); k++) {
                    long oidCateg = Long.parseLong((String) srcSaleReport.getListCategoryId().get(k));
                    if (categoryId.length() > 0) {
                        categoryId = categoryId + " or cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    } else {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    }
                }
                categoryId = "(" + categoryId + ")";
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListMerkId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListMerkId().size(); k++) {
                    long oidMerk = Long.parseLong((String) srcSaleReport.getListMerkId().get(k));
                    if (markId.length() > 0) {
                        markId = markId + " or merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    } else {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    }
                }
                markId = "(" + markId + ")";
            }
            
            String transType = "";
            //if (srcSaleReport.getTransType() >= 0) {
            if (srcSaleReport.getTransType() >= 0 && srcSaleReport.getTransType()<6) {
                int[] transMode = getTransMode(srcSaleReport);
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] +
                           // " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE];
                             //add by Mirah
                              " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] +
                              " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS];
                
                            //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS] +
                            //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_TYPE]+
            }

            //if trans type cash credit
            else if (srcSaleReport.getTransType()== 6){
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")" ;
            }
			
			else if (srcSaleReport.getTransType()== 7){
                transType = " (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")";
								
				if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER){
					String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
					String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
					
					transType += " AND (cbmp.BILL_DATE IS NULL OR DATE_FORMAT(cbmp.BILL_DATE,\"%Y-%m-%d\") != DATE_FORMAT(cbm.BILL_DATE,\"%Y-%m-%d\") OR cbm.DOC_TYPE = 0)" 
							+ " AND "
							+ " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+ " NOT IN "
							+ "(SELECT cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]
							+ " FROM "+PstBillDetail.TBL_CASH_BILL_DETAIL +" cbd "
							+ " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN+ " cbm "
							+ " ON cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+ " = cbm."+ PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
							+ " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN+ " cbm1 "
							+ " ON cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+ " = cbm1."+ PstBillMain.fieldNames[PstBillMain.FLD_PARENT_ID]
							+ " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL +" cbd1 "
							+ " ON cbm1."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " = cbd1."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
							+ " AND cbd1."+ PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +" = cbd."+ PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
							+ " WHERE cbm."+ PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
							+ " BETWEEN '"+startDate+" 00:00:00' AND '"+endDate+" 23:59:00' AND cbm1."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=1"
							+ " AND DATE_FORMAT(cbm1."+ PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+", \"%Y-%m-%d\") = DATE_FORMAT(cbm."+ PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+", \"%Y-%m-%d\"))";
				}
					
            }

            
            /*
            if(transType.length()>0)
                transType = transType + " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
            else
                transType = transType + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
             */
            
            //String strBillStatus = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            
            String whereClause = "";
            if (locationId.length() > 0)
                whereClause = locationId;
            
            if (categoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + categoryId;
                } else {
                    whereClause = whereClause + categoryId;
                }
            }
            
            if (dateTrans.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + dateTrans;
                } else {
                    whereClause = whereClause + dateTrans;
                }
            }
            
            if (itemName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemName;
                } else {
                    whereClause = whereClause + itemName;
                }
            }
            
            if (itemId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemId;
                } else {
                    whereClause = whereClause + itemId;
                }
            }
            
            if (locationId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + locationId;
                } else {
                    whereClause = whereClause + locationId;
                }
            }
            
            if (markId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + markId;
                } else {
                    whereClause = whereClause + markId;
                }
            }
            
            if (salesPersonId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesPersonId;
                } else {
                    whereClause = whereClause + salesPersonId;
                }
            }
            
            if (shiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + shiftId;
                } else {
                    whereClause = whereClause + shiftId;
                }
            }
            
            if (subCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + subCategoryId;
                } else {
                    whereClause = whereClause + subCategoryId;
                }
            }
            
            if (supplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + supplierId;
                } else {
                    whereClause = whereClause + supplierId;
                }
            }
            
            if (transType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transType;
                } else {
                    whereClause = whereClause + transType;
                }
            }
			
			/*if (whereClause.length() > 0) {
					String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
					String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                    whereClause = whereClause + " AND "
							+ " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+ " NOT IN "
							+ "(SELECT cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]
							+ " FROM "+PstBillDetail.TBL_CASH_BILL_DETAIL +" cbd "
							+ " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN+ " cbm "
							+ " ON cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+ " = cbm."+ PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
							+ " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN+ " cbm1 "
							+ " ON cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+ " = cbm1."+ PstBillMain.fieldNames[PstBillMain.FLD_PARENT_ID]
							+ " WHERE cbm."+ PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
							+ " BETWEEN '"+startDate+" 00:00:00' AND '"+endDate+" 23:59:00' AND cbm1."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=1)";
                } else {
					String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
					String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                    whereClause = whereClause + " cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]+ " NOT IN "
							+ "(SELECT cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]
							+ " FROM "+PstBillDetail.TBL_CASH_BILL_DETAIL +" cbd INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN
							+ " cbm ON cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+ " = cbm."
							+ PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+ " WHERE cbm."+ PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
							+ " BETWEEN '"+startDate+"' AND '"+endDate+"' AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=1)";;
                }*/
            
            //if (strBillStatus.length() > 0) {
                //if (whereClause.length() > 0) {
                   // whereClause = whereClause + " AND " + strBillStatus;
                //} else {
                    //whereClause = whereClause + strBillStatus;
               // }
           // }
            
            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause; //+" AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            } else {
                sql = sql + " where cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }
            
            /* hide dulu opie-eyek 20160806*/
            switch (srcSaleReport.getGroupBy()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    sql = sql + "\n  group by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_LOCATION:
                    sql = sql + "\n  group by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    sql = sql + "\n  group by sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    sql = sql + "\n  group by shift." + PstShift.fieldNames[PstShift.FLD_START_TIME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    sql = sql + "\n  group by sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_ITEM:
                    sql = sql + "\n  group by "
                            //+ " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]
                            + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                            //+ ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]
                            + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                            //+ ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]
                            + "";
                    break;
                    
                case SrcSaleReport.GROUP_BY_DATE:
                    sql = sql + "\n  group by mat." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK: 
                    sql = sql + "\n  group by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                    break;
                    
                default:
                    sql = sql + "\n  group by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
            }
            
            String childGroup = "";
            switch (srcSaleReport.getDetailGroup()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    childGroup = "\n  cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_LOCATION:
                    childGroup = "\n  loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    childGroup = "\n  sps." + PstSales.fieldNames[PstSales.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    childGroup = "\n  shift." + PstShift.fieldNames[PstShift.FLD_START_TIME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    childGroup = "\n  sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_ITEM:
                    childGroup = "\n  mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_DATE:
                    childGroup = "\n  cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK:
                    childGroup = "\n  merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                    break;
                    
                default:
                    childGroup = "\n  mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
            }
            
            String descSort = "";
            if (srcSaleReport.getDescSort() == SrcSaleReport.SORT_DESC) {
                descSort = " DESC ";
            } else {
                descSort = " ASC ";
            }
            
            switch (srcSaleReport.getSortBy()) {
                case SrcSaleReport.SORT_BY_DATE:
                    sql = sql + "\n  order by cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_LOCATION:
                    sql = sql + "\n  order by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARK:
                    sql = sql + "\n  order by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SALES_PERSON:
                    sql = sql + "\n  order by sps." + PstSales.fieldNames[PstSales.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SHIFT:
                    sql = sql + "\n  order by shift." + PstShift.fieldNames[PstShift.FLD_START_TIME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SUB_CATEGORY:
                    sql = sql + "\n  order by scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_CATEGORY:
                    sql = sql + "\n  order by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SUPPLIER:
                    sql = sql + "\n  order by supplier " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_ITEM:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_TOTAL_QTY:
                    sql = sql + "\n  order by cbd_qty " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_TOTAL_SALE:
                    sql = sql + "\n  order by cbd_total " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARGIN:
                    sql = sql + "\n  order by cbd_margin " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARGIN_PCT:
                    sql = sql + "\n  order by cbd_margin_pct " + descSort + " ";
                    break;
                    
                default:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
            }
            
            sql += ", mt.MASTER_NAME , cbm.BILL_DATE";
            
            if(recordToGet!=0){
                sql = sql + "\n  limit 0,"+recordToGet+"";
            }
            
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //Vector temp = new Vector(1,1);
                SaleReportItem item = new SaleReportItem();
                //Unit unit = new Unit();
                if (srcSaleReport.getQueryType()== SrcSaleReport.GROUP_BY_CATEGORY) {
                    item.setCategoryId(rs.getLong("cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ""));
                    item.setCategoryName(rs.getString("cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + ""));
                }
                
                item.setItemId(rs.getLong("mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ""));
                item.setItemCode(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ""));
                item.setItemName(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ""));
                item.setItemBarcode(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + ""));
                String matName = "";
                try {
                    Material mats = PstMaterial.fetchExc(item.getItemId());
                    matName = mats.getName();
                    item.setItemName(matName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setItemQty(rs.getDouble("cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ""));
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_LOCATION) {
                    item.setLocationId(rs.getLong("loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ""));
                    item.setLocationName(rs.getString("loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_MARK) {
                    item.setMarkId(rs.getLong("merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + ""));
                    item.setMarkName(rs.getString("merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SALES) {
                    item.setSalesPersonId(rs.getLong("sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + ""));
                    item.setSalesPersonName(rs.getString("sps." + PstSales.fieldNames[PstSales.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                    item.setShiftId(rs.getLong("shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ""));
                    item.setShiftName(rs.getString("shift." + PstShift.fieldNames[PstShift.FLD_NAME] + ""));
                    item.setTotTransaksi(rs.getInt("tot_transaksi"));
                }else{
                    if(srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                        try{
                            item.setShiftId(rs.getLong("shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ""));
                            item.setShiftName(rs.getString("shift." + PstShift.fieldNames[PstShift.FLD_NAME] + ""));
                            item.setTotTransaksi(rs.getInt("tot_transaksi"));
                        }catch(Exception es){
                        }
                    }
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SUB_CATEGORY) {
                    item.setSubCategoryId(rs.getLong("scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + ""));
                    item.setSubCategoryName(rs.getString("scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                    item.setSupplierId(rs.getLong("supplierId"));
                    item.setSupplierName(rs.getString("supplier"));
                }
                
                item.setDiscount(rs.getDouble("cbd_disc"));
                item.setTotalQty(rs.getDouble("cbd_qty"));
                item.setTotalSold(rs.getDouble("cbd_total"));
                item.setTotalCost(rs.getDouble("cbd_cost"));
                item.setTotalMargin(rs.getDouble("cbd_margin"));
                item.setTotalMarginPct(rs.getDouble("cbd_margin_pct"));
                item.setTotalQtyByStock(rs.getDouble("cbd_qty_stock"));
                item.setTotalSoldByStock(rs.getDouble("cbd_item_price_stock"));
                //temp.add(item);
                
                //unit.setCode(rs.getString("unit."+PstUnit.fieldNames[PstUnit.FLD_CODE]));
                //temp.add(unit);
                
                //added by dewok 20181207 for greenbowl sales report
                item.setBillNumber(rs.getString("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                item.setBillDate(rs.getDate("cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                item.setPrice(rs.getDouble("cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]));
                item.setMasterTypeId(rs.getLong("MASTER_TYPE_ID"));
                item.setMasterTypeName(rs.getString("PROMOSI"));
                result.add(item);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
     public static Vector getListSpesialRequest(SrcSaleReport srcSaleReport, int start, int recordToGet, int logMode) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            Shift shift = null;
            String stTime = "";
            String edTime = "";
            Date dtTo = new Date();
            stTime = "00:00:00";
            edTime = "23:59:59";
            
            String sql = "";
            switch (srcSaleReport.getQueryType()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    sql = "SELECT cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "," +
                            " cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_category as cat" +
                            " on cat.category_id =mat.category_id)";
                    break;

                case SrcSaleReport.GROUP_BY_LOCATION:
                    sql = sql + "SELECT mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "," +
                            " loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin, " +
                            " ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM ";
                    sql = sql + "cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id" +
                            //" inner join location as loc" +
                            //" on loc.location_id = cbm.location_id" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id";
                    if (srcSaleReport.getCategoryId() != 0) {
                        sql = sql + " inner join pos_category as cat" +
                                " on cat.category_id = mat.category_id";
                    }
                    if (srcSaleReport.getMarkId() != 0) {
                        sql = sql + " inner join pos_merk as merk" +
                                " on merk.merk_id = mat.merk_id";
                    }
                    System.out.println("-->> GROUP_BY_LOCATION : ");
                    break;		    

                case SrcSaleReport.GROUP_BY_SALES:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "," +
                            " sps." + PstSales.fieldNames[PstSales.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.item_price * cbd.qty * cbm.rate) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,sum(cbd.cost * cbd.qty) as cbd_margin_pct" +
                            " ,count(distinct(cbd.cash_bill_main_id)) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join " +
                            " pos_sales_person as sps" +
                            " on sps.sales_code = cbm.sales_code)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_SHIFT:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "," +
                            " shift." + PstShift.fieldNames[PstShift.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join " +
                            " pos_shift as shift" +
                            " on cbm.shift_id = shift.shift_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "," +
                            " sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM ((((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_vendor_price as vpc" +
                            " on vpc.material_id= mat.material_id)" +
                            " inner join contact_list as sup" +
                            " on vpc.vendor_id = sup.contact_id)";
                    break;

                case SrcSaleReport.GROUP_BY_DATE:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM ((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_ITEM:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_category as cat" +
                            " on cat.category_id =mat.category_id)";
                    break;

                case SrcSaleReport.GROUP_BY_MARK:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "," +
                            " merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_merk as merk" +
                            " on merk.merk_id = mat.merk_id)";
                    break;
            }
            
            /** melakukan inner ke table lokasi, karena setiap transaksi pasti memiliki lokasi transaksi (gwawan@dimata 20080114)*/
            sql += " inner join location as loc" + " on loc.location_id = cbm.location_id";
            
            String dateTrans = "";
            if (srcSaleReport.getDateFrom() != null) {
                String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                dateTrans = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
            }

            String itemId = "";
            if (srcSaleReport.getItemId() > 0) {
                itemId = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "=" + srcSaleReport.getItemId();
            }

            String itemName = "";
            if (srcSaleReport.getItemName().length() > 0) {
                itemName = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "=" + srcSaleReport.getItemName();
            }

            String locationId = "";
            if (srcSaleReport.getLocationId() != 0) {
                locationId = " loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
            }
            
            String categoryId = "";
            String subCategoryId = "";
            String salesPersonId = "";
            String shiftId = "";
            String supplierId = "";
            String markId = "";
            
//            /** kondisi berdasarkan jenis group yang dipilih */
//            switch (srcSaleReport.getQueryType()) {
//                case SrcSaleReport.GROUP_BY_CATEGORY:
//                    if (srcSaleReport.getCategoryId() != 0) {
//                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + srcSaleReport.getCategoryId();
//                    }
//                    break;
//                    
//                case SrcSaleReport.GROUP_BY_SUB_CATEGORY:
//                    if (srcSaleReport.getSubCategoryId() != 0) {
//                        subCategoryId = " scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + "=" + srcSaleReport.getSubCategoryId();
//                    }
//                    break;
//                    
//                case SrcSaleReport.GROUP_BY_SALES:
//                    if (srcSaleReport.getSalesPersonId() != 0) {
//                        salesPersonId = " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "=" + srcSaleReport.getSalesPersonId();
//                    }
//                    break;
//                    
//                case SrcSaleReport.GROUP_BY_SHIFT:
//                    if (srcSaleReport.getShiftId() != 0) {
//                        shiftId = " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
//                    }
//                    break;
//                    
//                case SrcSaleReport.GROUP_BY_SUPPLIER:
//                    if (srcSaleReport.getSupplierId() != 0) {
//                        supplierId = " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "=" + srcSaleReport.getSupplierId();
//                    }
//                    break;
//                    
//                case SrcSaleReport.GROUP_BY_MARK:
//                    if (srcSaleReport.getMarkId() != 0) {
//                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + srcSaleReport.getMarkId();
//                    }
//                    break;
//                    
//                default:
//                    break;
//            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListCategoryId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListCategoryId().size(); k++) {
                    long oidCateg = Long.parseLong((String) srcSaleReport.getListCategoryId().get(k));
                    if (categoryId.length() > 0) {
                        categoryId = categoryId + " or cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    } else {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    }
                }
                categoryId = "(" + categoryId + ")";
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListMerkId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListMerkId().size(); k++) {
                    long oidMerk = Long.parseLong((String) srcSaleReport.getListMerkId().get(k));
                    if (markId.length() > 0) {
                        markId = markId + " or merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    } else {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    }
                }
                markId = "(" + markId + ")";
            }
            
            String transType = "";
            if (srcSaleReport.getTransType() >= 0 && srcSaleReport.getTransType()<6) {
                int[] transMode = getTransMode(srcSaleReport);
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] +
                              " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] +
                              " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS];
            }

            //if trans type cash credit
            else if (srcSaleReport.getTransType()== 6){
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")" ;
            }

            String whereClause = "";
            if (locationId.length() > 0)
                whereClause = locationId;
            
            if (categoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + categoryId;
                } else {
                    whereClause = whereClause + categoryId;
                }
            }
            
            if (dateTrans.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + dateTrans;
                } else {
                    whereClause = whereClause + dateTrans;
                }
            }
            
            if (itemName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemName;
                } else {
                    whereClause = whereClause + itemName;
                }
            }
            
            if (itemId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemId;
                } else {
                    whereClause = whereClause + itemId;
                }
            }
            
            if (locationId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + locationId;
                } else {
                    whereClause = whereClause + locationId;
                }
            }
            
            if (markId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + markId;
                } else {
                    whereClause = whereClause + markId;
                }
            }
            
            if (salesPersonId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesPersonId;
                } else {
                    whereClause = whereClause + salesPersonId;
                }
            }
            
            if (shiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + shiftId;
                } else {
                    whereClause = whereClause + shiftId;
                }
            }
            
            if (subCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + subCategoryId;
                } else {
                    whereClause = whereClause + subCategoryId;
                }
            }
            
            if (supplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + supplierId;
                } else {
                    whereClause = whereClause + supplierId;
                }
            }
            
            if (transType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transType;
                } else {
                    whereClause = whereClause + transType;
                }
            }
            
            if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " +  PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "=3";
            } else {
                whereClause = whereClause +  PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + "=3";
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause; //+" AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            } else {
                sql = sql + " where cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }
            
            
            
            sql = sql + "\n  group by cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME];
            
            
            String descSort = "";
            if (srcSaleReport.getDescSort() == SrcSaleReport.SORT_DESC) {
                descSort = " DESC ";
            } else {
                descSort = " ASC ";
            }
            
            switch (srcSaleReport.getSortBy()) {
                case SrcSaleReport.SORT_BY_DATE:
                    sql = sql + "\n  order by cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_LOCATION:
                    sql = sql + "\n  order by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARK:
                    sql = sql + "\n  order by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SALES_PERSON:
                    sql = sql + "\n  order by sps." + PstSales.fieldNames[PstSales.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SHIFT:
                    sql = sql + "\n  order by shift." + PstShift.fieldNames[PstShift.FLD_START_TIME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SUB_CATEGORY:
                    sql = sql + "\n  order by scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_CATEGORY:
                    sql = sql + "\n  order by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_SUPPLIER:
                    sql = sql + "\n  order by sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_ITEM:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_TOTAL_QTY:
                    sql = sql + "\n  order by cbd_qty " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_TOTAL_SALE:
                    sql = sql + "\n  order by cbd_total " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARGIN:
                    sql = sql + "\n  order by cbd_margin " + descSort + " ";
                    break;
                    
                case SrcSaleReport.SORT_BY_MARGIN_PCT:
                    sql = sql + "\n  order by cbd_margin_pct " + descSort + " ";
                    break;
                    
                default:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
            }
            
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //Vector temp = new Vector(1,1);
                SaleReportItem item = new SaleReportItem();
                //Unit unit = new Unit();
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
                    item.setCategoryId(rs.getLong("cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ""));
                    item.setCategoryName(rs.getString("cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + ""));
                }
                
                item.setItemId(rs.getLong("mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ""));
                item.setItemCode(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ""));
                item.setItemName(rs.getString("cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_NAME] + ""));
                String matName = "";
                try {
                    //Material mats = PstMaterial.fetchExc(item.getItemId());
                    matName = item.getItemName();
                    item.setItemName(matName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setItemQty(rs.getDouble("cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ""));
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_LOCATION) {
                    item.setLocationId(rs.getLong("loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ""));
                    item.setLocationName(rs.getString("loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_MARK) {
                    item.setMarkId(rs.getLong("merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + ""));
                    item.setMarkName(rs.getString("merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SALES) {
                    item.setSalesPersonId(rs.getLong("sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + ""));
                    item.setSalesPersonName(rs.getString("sps." + PstSales.fieldNames[PstSales.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SHIFT) {
                    item.setShiftId(rs.getLong("shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ""));
                    item.setShiftName(rs.getString("shift." + PstShift.fieldNames[PstShift.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SUB_CATEGORY) {
                    item.setSubCategoryId(rs.getLong("scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + ""));
                    item.setSubCategoryName(rs.getString("scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                    item.setSupplierId(rs.getLong("sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + ""));
                    item.setSupplierName(rs.getString("sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + ""));
                }
                
                item.setDiscount(rs.getDouble("cbd_disc"));
                item.setTotalQty(rs.getDouble("cbd_qty"));
                item.setTotalSold(rs.getDouble("cbd_total"));
                item.setTotalCost(rs.getDouble("cbd_cost"));
                item.setTotalMargin(rs.getDouble("cbd_margin"));
                item.setTotalMarginPct(rs.getDouble("cbd_margin_pct"));
                item.setTotalQtyByStock(rs.getDouble("cbd_qty_stock"));
                item.setTotalSoldByStock(rs.getDouble("cbd_item_price_stock"));
                
                result.add(item);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    
    /*************/
    public static Vector getGrandTotalList(SrcSaleReport srcSaleReport, int logMode) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            Shift shift = null;
            String stTime = "";
            String edTime = "";
            Date dtTo = new Date();
            if (srcSaleReport.getShiftId() > 0) {
                shift = PstShift.fetchExc(srcSaleReport.getShiftId());
                stTime = Formater.formatDate(shift.getStartTime(), "HH:mm:00");
                edTime = Formater.formatDate(shift.getEndTime(), "HH:mm:00");
                dtTo = new Date(srcSaleReport.getDateFrom().getTime());
                if (shift.getStartTime().getHours() > shift.getEndTime().getHours()) {
                    dtTo.setDate(dtTo.getDate() + 1);
                    //srcSaleReport.setDateTo(dtTo);
                } else {
                    //srcSaleReport.setDateTo(dtTo);
                }
            } else {
                stTime = "00:00:00";
                edTime = "23:59:59";
            }
            
            String sql = "";
            switch (srcSaleReport.getQueryType()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    sql = "SELECT cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "," +
                            " cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                            " ((sum(cbd.total_price * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_category as cat" +
                            " on cat.category_id =mat.category_id)";
                    break;

                case SrcSaleReport.GROUP_BY_LOCATION:
                    sql = sql + "SELECT mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "," +
                            " loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total," +
                            " (sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin, " +
                            " ((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM " + "cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id";
                    if (srcSaleReport.getCategoryId() != 0) {
                        sql = sql + " inner join pos_category as cat" +
                                " on cat.category_id = mat.category_id";
                    }
                    if (srcSaleReport.getMarkId() != 0) {
                        sql = sql + " inner join pos_merk as merk" +
                                " on merk.merk_id = mat.merk_id";
                    }
                    System.out.println("-->> GROUP_BY_LOCATION : ");
                    break;

                case SrcSaleReport.GROUP_BY_SALES:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "," +
                            " sps." + PstSales.fieldNames[PstSales.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.item_price * cbd.qty * cbm.rate) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,sum(cbd.cost * cbd.qty) as cbd_margin_pct" +
                            " ,count(distinct(cbd.cash_bill_main_id)) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join " +
                            " pos_sales_person as sps" +
                            " on sps.sales_code = cbm.sales_code)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_SHIFT:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "," +
                            " shift." + PstShift.fieldNames[PstShift.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join " +
                            " pos_shift as shift" +
                            " on cbm.shift_id = shift.shift_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "," +
                            " sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM ((((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_vendor_price as vpc" +
                            " on vpc.material_id= mat.material_id)" +
                            " inner join contact_list as sup" +
                            " on vpc.vendor_id = sup.contact_id)";
                    break;

                case SrcSaleReport.GROUP_BY_DATE:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM ((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)";
                    break;

                case SrcSaleReport.GROUP_BY_ITEM:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_category as cat" +
                            " on cat.category_id =mat.category_id)";
                    break;

                case SrcSaleReport.GROUP_BY_MARK:
                    sql = "SELECT " +
                            " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                            " cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + "," +
                            " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "," +
                            " merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + "," +
                            " sum(cbd.qty) as cbd_qty," +
                            " sum(cbd.cost * cbd.qty) as cbd_cost," +
                            " sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) as cbd_total" +
                            " ,(sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty)) as cbd_margin " +
                            " ,((sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate) - sum(cbd.cost * cbd.qty))/sum((cbd.total_price - (((cbm.disc / (cbm.amount+cbm.disc)) * (cbd.item_price * cbd.qty)))) * cbm.rate * cbd.qty)* 100 ) as cbd_margin_pct," +
                            " sum(cbd.disc) as cbd_disc," +
                            " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_QTY_STOCK]+") as cbd_qty_stock"+
                            ", sum(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE_STOCK]+") as cbd_item_price_stock"+
                            " FROM (((cash_bill_main as cbm" +
                            " inner join cash_bill_detail as cbd " +
                            " on cbm.cash_bill_main_id = cbd.cash_bill_main_id)" +
                            " inner join pos_material as mat" +
                            " on mat.material_id= cbd.material_id)" +
                            " inner join pos_merk as merk" +
                            " on merk.merk_id = mat.merk_id)";
                    break;
            }
            
            /** melakukan inner ke table lokasi, karena setiap transaksi pasti memiliki lokasi transaksi (gwawan@dimata 20080114)*/
            sql += " inner join location as loc" + " on loc.location_id = cbm.location_id";
            
            String dateTrans = "";
            if (srcSaleReport.getDateFrom() != null) {
                String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                dateTrans = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
            }

            String itemId = "";
            if (srcSaleReport.getItemId() > 0) {
                itemId = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "=" + srcSaleReport.getItemId();
            }

            String itemName = "";
            if (srcSaleReport.getItemName().length() > 0) {
                itemName = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "=" + srcSaleReport.getItemName();
            }

            String locationId = "";
            if (srcSaleReport.getLocationId() != 0) {
                locationId = " loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
            }
            
            String categoryId = "";
            String subCategoryId = "";
            String salesPersonId = "";
            String shiftId = "";
            String supplierId = "";
            String markId = "";
            
            /** kondisi berdasarkan jenis group yang dipilih */
            switch (srcSaleReport.getQueryType()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    if (srcSaleReport.getCategoryId() != 0) {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + srcSaleReport.getCategoryId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUB_CATEGORY:
                    if (srcSaleReport.getSubCategoryId() != 0) {
                        subCategoryId = " scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + "=" + srcSaleReport.getSubCategoryId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    if (srcSaleReport.getSalesPersonId() != 0) {
                        salesPersonId = " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "=" + srcSaleReport.getSalesPersonId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    if (srcSaleReport.getShiftId() != 0) {
                        shiftId = " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    if (srcSaleReport.getSupplierId() != 0) {
                        supplierId = " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "=" + srcSaleReport.getSupplierId();
                    }
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK:
                    if (srcSaleReport.getMarkId() != 0) {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + srcSaleReport.getMarkId();
                    }
                    break;
                    
                default:
                    break;
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListCategoryId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListCategoryId().size(); k++) {
                    long oidCateg = Long.parseLong((String) srcSaleReport.getListCategoryId().get(k));
                    if (categoryId.length() > 0) {
                        categoryId = categoryId + " or cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    } else {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    }
                }
                categoryId = "(" + categoryId + ")";
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListMerkId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListMerkId().size(); k++) {
                    long oidMerk = Long.parseLong((String) srcSaleReport.getListMerkId().get(k));
                    if (markId.length() > 0) {
                        markId = markId + " or merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    } else {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    }
                }
                markId = "(" + markId + ")";
            }
            
            String transType = "";
            if (srcSaleReport.getTransType() >= 0) {
                int[] transMode = getTransMode(srcSaleReport);
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] +
                            " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE];
                            //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS] +
                            //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_TYPE]+
            }
            
            String strBillStatus = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            
            String whereClause = "";
            if (locationId.length() > 0)
                whereClause = locationId;
            
            if (categoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + categoryId;
                } else {
                    whereClause = whereClause + categoryId;
                }
            }
            
            if (dateTrans.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + dateTrans;
                } else {
                    whereClause = whereClause + dateTrans;
                }
            }
            
            if (itemName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemName;
                } else {
                    whereClause = whereClause + itemName;
                }
            }
            
            if (itemId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemId;
                } else {
                    whereClause = whereClause + itemId;
                }
            }
            
            if (locationId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + locationId;
                } else {
                    whereClause = whereClause + locationId;
                }
            }
            
            if (markId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + markId;
                } else {
                    whereClause = whereClause + markId;
                }
            }
            
            if (salesPersonId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesPersonId;
                } else {
                    whereClause = whereClause + salesPersonId;
                }
            }
            
            if (shiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + shiftId;
                } else {
                    whereClause = whereClause + shiftId;
                }
            }
            
            if (subCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + subCategoryId;
                } else {
                    whereClause = whereClause + subCategoryId;
                }
            }
            
            if (supplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + supplierId;
                } else {
                    whereClause = whereClause + supplierId;
                }
            }
            
            if (transType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transType;
                } else {
                    whereClause = whereClause + transType;
                }
            }
            
            if (strBillStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strBillStatus;
                } else {
                    whereClause = whereClause + strBillStatus;
                }
            }
            
            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause; //+" AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            } else {
                sql = sql + " where cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }
            
            switch (srcSaleReport.getGroupBy()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    sql = sql + "\n  group by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_LOCATION:
                    sql = sql + "\n  group by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    sql = sql + "\n  group by sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    sql = sql + "\n  group by shift." + PstShift.fieldNames[PstShift.FLD_START_TIME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    sql = sql + "\n  group by sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_ITEM:
                    sql = sql + "\n  group by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
                    
                case SrcSaleReport.GROUP_BY_DATE:
                    sql = sql + "\n  group by mat." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK:
                    sql = sql + "\n  group merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                    break;
                    
                default:
                    sql = sql + "\n  group by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                            ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
            }
            
            String childGroup = "";
            switch (srcSaleReport.getDetailGroup()) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    childGroup = "\n  cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_LOCATION:
                    childGroup = "\n  loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SALES:
                    childGroup = "\n  sps." + PstSales.fieldNames[PstSales.FLD_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SHIFT:
                    childGroup = "\n  shift." + PstShift.fieldNames[PstShift.FLD_START_TIME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    childGroup = "\n  sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " ";
                    break;
                    
                case SrcSaleReport.GROUP_BY_ITEM:
                    childGroup = "\n  mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
                    
                case SrcSaleReport.GROUP_BY_DATE:
                    childGroup = "\n  cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                    break;
                    
                case SrcSaleReport.GROUP_BY_MARK:
                    childGroup = "\n  merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                    break;
                    
                default:
                    childGroup = "\n  mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
            }
            
            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //Vector temp = new Vector(1,1);
                SaleReportItem item = new SaleReportItem();
                //Unit unit = new Unit();
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
                    item.setCategoryId(rs.getLong("cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ""));
                    item.setCategoryName(rs.getString("cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + ""));
                }
                
                item.setItemId(rs.getLong("mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ""));
                item.setItemCode(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ""));
                item.setItemName(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ""));
                String matName = "";
                try {
                    Material mats = PstMaterial.fetchExc(item.getItemId());
                    matName = mats.getName();
                    item.setItemName(matName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setItemQty(rs.getDouble("cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ""));
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_LOCATION) {
                    item.setLocationId(rs.getLong("loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ""));
                    item.setLocationName(rs.getString("loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_MARK) {
                    item.setMarkId(rs.getLong("merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + ""));
                    item.setMarkName(rs.getString("merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SALES) {
                    item.setSalesPersonId(rs.getLong("sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + ""));
                    item.setSalesPersonName(rs.getString("sps." + PstSales.fieldNames[PstSales.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SHIFT) {
                    item.setShiftId(rs.getLong("shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ""));
                    item.setShiftName(rs.getString("shift." + PstShift.fieldNames[PstShift.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SUB_CATEGORY) {
                    item.setSubCategoryId(rs.getLong("scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + ""));
                    item.setSubCategoryName(rs.getString("scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + ""));
                }
                
                if (srcSaleReport.getGroupBy() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                    item.setSupplierId(rs.getLong("sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + ""));
                    item.setSupplierName(rs.getString("sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + ""));
                }
                
                item.setDiscount(rs.getDouble("cbd_disc"));
                item.setTotalQty(rs.getDouble("cbd_qty"));
                item.setTotalSold(rs.getDouble("cbd_total"));
                item.setTotalCost(rs.getDouble("cbd_cost"));
                item.setTotalMargin(rs.getDouble("cbd_margin"));
                item.setTotalMarginPct(rs.getDouble("cbd_margin_pct"));
                item.setTotalQtyByStock(rs.getDouble("cbd_qty_stock"));
                item.setTotalSoldByStock(rs.getDouble("cbd_item_price_stock"));
                //temp.add(item);
                
                //unit.setCode(rs.getString("unit."+PstUnit.fieldNames[PstUnit.FLD_CODE]));
                //temp.add(unit);
                
                result.add(item);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    /*************/

    public static Vector getDiscInvoice(SrcSaleReport srcSaleReport, int start, int recordToGet, int logMode) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            Shift shift = null;
            String stTime = "";
            String edTime = "";
            Date dtTo = new Date();
            if (srcSaleReport.getShiftId() > 0) {
                stTime = "00:00:00";
                edTime = "23:59:59";
            } else {
                stTime = "00:00:00";
                edTime = "23:59:59";
            }

            String sql = "";
            sql = "select mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                    " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," +
                    " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                    " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                    " sum(cbd.qty) as cbd_qty," +
                    " sum(cbd.cost * cbd.qty) as cbd_cost," +
                    " sum(cbd.total_price) as cbd_total,";

                String startDate = "";
                String endDate = "";
                    if (srcSaleReport.getDateFrom() != null) {
                        startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
                        endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                    }

                    sql = sql + " (cbm.disc/(select sum(dt.qty) from cash_bill_detail as dt inner join cash_bill_main as bm " +
                            " on dt.cash_bill_main_id = bm.cash_bill_main_id "+
                            "where bm.cash_bill_main_id = cbm.cash_bill_main_id " +
                            "and bm.bill_date between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
                    if (srcSaleReport.getLocationId() != 0) {
                        sql = sql + " and bm.location_id = "+srcSaleReport.getLocationId()+") * sum(cbd.qty)) as disc_total,";
                    }else{
                        sql = sql + ") * sum(cbd.qty)) as disc_total,";
                    }

                    sql = sql + " (sum(cbd.total_price) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                    " ((sum(cbd.total_price) - sum(cbd.cost * cbd.qty)) / sum(cbd.total_price * cbd.qty)* 100 ) as cbd_margin_pct";

            if (srcSaleReport.getViewColoumChange().size() > 0) {
                for (int i = 0; i < srcSaleReport.getViewColoumChange().size(); i++) {
                    int coloum = Integer.parseInt((String) srcSaleReport.getViewColoumChange().get(i));
                    switch (coloum) {
                        case SrcSaleReport.SHOW_FIELD_CATEGORY:
                            sql = sql + ", cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "," +
                                    " cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_MERK:
                            sql = sql + ", merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "," +
                                    " merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SELL_PRICE:
                            sql = sql + ", (cbd.item_price - cbd.disc) as cbd_price ";
                            break;
                        case SrcSaleReport.SHOW_FIELD_TOTAL_SALE:
                            sql = sql + ", sum(cbd.total_price) as cbd_total ";
                            break;
                        case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                            sql = sql + ", sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "," +
                                    " sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_BUYING_PRICE:
                            sql = sql + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                            sql = sql + ", sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "," +
                                    " sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_LOCATION:
                            sql = sql + ", loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "," +
                                    " loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SHIFT:
                            sql = sql + ", shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "," +
                                    " shift." + PstShift.fieldNames[PstShift.FLD_NAME];
                            break;
                    }
                }
            }

            sql = sql + " FROM cash_bill_main as cbm" +
                    " inner join cash_bill_detail as cbd " +
                    " on cbm.cash_bill_main_id = cbd.cash_bill_main_id" +
                    " inner join pos_material as mat" +
                    " on mat.material_id= cbd.material_id";

            if (srcSaleReport.getViewColoumChange().size() > 0) {
                for (int i = 0; i < srcSaleReport.getViewColoumChange().size(); i++) {
                    int coloum = Integer.parseInt((String) srcSaleReport.getViewColoumChange().get(i));
                    switch (coloum) {
                        case SrcSaleReport.SHOW_FIELD_CATEGORY:
                            sql = sql + " inner join pos_category as cat" +
                                    " on cat.category_id = mat.category_id";
                            break;
                        case SrcSaleReport.SHOW_FIELD_MERK:
                            sql = sql + " inner join pos_merk as merk" +
                                    " on merk.merk_id = mat.merk_id";
                            break;
                        case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                            sql = sql + " inner join pos_vendor_price as vpc" +
                                    " on vpc.material_id= mat.material_id" +
                                    " inner join contact_list as sup" +
                                    " on vpc.vendor_id = sup.contact_id";
                            break;
                        case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                            sql = sql + " left join pos_sales_person as sps" +
                                    " on sps.sales_code = cbm.sales_code";
                            break;
                        case SrcSaleReport.SHOW_FIELD_LOCATION:
                            sql = sql + " inner join location as loc" +
                                    " on loc.location_id = cbm.location_id";
                            break;
                        case SrcSaleReport.SHOW_FIELD_SHIFT:
                            sql = sql + " inner join pos_shift as shift" +
                                    " on cbm.shift_id = shift.shift_id";

                            break;
                    }
                }
            }

            String categoryId = "";
            if (srcSaleReport.getCategoryId() != 0) {
                // categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + srcSaleReport.getCategoryId();
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListCategoryId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListCategoryId().size(); k++) {
                    long oidCateg = Long.parseLong((String) srcSaleReport.getListCategoryId().get(k));
                    if (categoryId.length() > 0) {
                        categoryId = categoryId + " or cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    } else {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    }
                }
                categoryId = "(" + categoryId + ")";
            }

            String dateTrans = "";
            if (srcSaleReport.getDateFrom() != null) {
                startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
                endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                dateTrans = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
            }

            String itemId = "";
            if (srcSaleReport.getItemId() > 0) {
                itemId = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "=" + srcSaleReport.getItemId();
            }

            String itemName = "";
            if (srcSaleReport.getItemName().length() > 0) {
                itemName = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "=" + srcSaleReport.getItemName();
            }

            String locationId = "";
            if (srcSaleReport.getLocationId() != 0) {
                locationId = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
            }

            String markId = "";
            if (srcSaleReport.getMarkId() != 0) {
                markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + srcSaleReport.getMarkId();
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListMerkId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListMerkId().size(); k++) {
                    long oidMerk = Long.parseLong((String) srcSaleReport.getListMerkId().get(k));
                    if (markId.length() > 0) {
                        markId = markId + " or merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    } else {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    }
                }
                markId = "(" + markId + ")";
            }

            String salesPersonId = "";
            if (srcSaleReport.getSalesPersonId() != 0) {
                salesPersonId = " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "=" + srcSaleReport.getSalesPersonId();
            }

            String shiftId = "";
            if (srcSaleReport.getShiftId() != 0) {
                shiftId = " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
            }

            String subCategoryId = "";
            if (srcSaleReport.getSubCategoryId() != 0) {
                subCategoryId = " scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + "=" + srcSaleReport.getSubCategoryId();
            }

            String supplierId = "";
            if (srcSaleReport.getSupplierId() != 0) {
                supplierId = " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "=" + srcSaleReport.getSupplierId();
            }

            String transType = "";
            if (srcSaleReport.getTransType() >= 0) {
                int[] transMode = getTransMode(srcSaleReport);
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] +
                        //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] +
                        " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS];
                //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_TYPE]+
            }

            String strBillStatus = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;

            String whereClause = "";
            if (locationId.length() > 0)
                whereClause = locationId;

            if (categoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + categoryId;
                } else {
                    whereClause = whereClause + categoryId;
                }
            }

            if (dateTrans.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + dateTrans;
                } else {
                    whereClause = whereClause + dateTrans;
                }
            }

            if (itemName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemName;
                } else {
                    whereClause = whereClause + itemName;
                }
            }

            if (itemId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemId;
                } else {
                    whereClause = whereClause + itemId;
                }
            }

            if (markId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + markId;
                } else {
                    whereClause = whereClause + markId;
                }
            }


            if (salesPersonId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesPersonId;
                } else {
                    whereClause = whereClause + salesPersonId;
                }
            }

            if (shiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + shiftId;
                } else {
                    whereClause = whereClause + shiftId;
                }
            }

            if (subCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + subCategoryId;
                } else {
                    whereClause = whereClause + subCategoryId;
                }
            }

            if (supplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + supplierId;
                } else {
                    whereClause = whereClause + supplierId;
                }
            }

            if (transType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transType;
                } else {
                    whereClause = whereClause + transType;
                }
            }

            if (strBillStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strBillStatus;
                } else {
                    whereClause = whereClause + strBillStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause; //+" AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            } else {
                sql = sql + " where cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }
            sql = sql + " group by mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+
                    ", cbd.item_price ";

            if (srcSaleReport.getViewColoumChange().size() > 0) {
                for (int i = 0; i < srcSaleReport.getViewColoumChange().size(); i++) {
                    int coloum = Integer.parseInt((String) srcSaleReport.getViewColoumChange().get(i));
                    switch (coloum) {
                        case SrcSaleReport.SHOW_FIELD_CATEGORY:
                            sql = sql + ", cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_MERK:
                            sql = sql + ", merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                            sql = sql + ", sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                            sql = sql + ", sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_LOCATION:
                            sql = sql + ", loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SHIFT:
                            sql = sql + ", shift." + PstShift.fieldNames[PstShift.FLD_NAME];
                            break;
                    }
                }
            }

            String descSort = "";
            if (srcSaleReport.getDescSort() == SrcSaleReport.SORT_DESC) {
                descSort = " DESC ";
            } else {
                descSort = " ASC ";
            }

            switch (srcSaleReport.getSortBy()) {
                case SrcSaleReport.SHOW_FIELD_CODE:
                    sql = sql + " order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
                case SrcSaleReport.SHOW_FIELD_NAME:
                    sql = sql + " order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_BUYING_PRICE:
                    sql = sql + " order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST];
                    break;
                case SrcSaleReport.SHOW_FIELD_QTY:
                    sql = sql + " order by cbd_qty";
                    break;
                case SrcSaleReport.SHOW_FIELD_TOTAL_SALE:
                    sql = sql + " order by cbd_total";
                    break;
                case SrcSaleReport.SHOW_FIELD_SELL_PRICE:
                    sql = sql + " order by cbd_price";
                    break;
                case SrcSaleReport.SHOW_FIELD_CATEGORY:
                    sql = sql + " order by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_MERK:
                    sql = sql + " order by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                    sql = sql + " order by sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                    break;
                case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                    sql = sql + " order by sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_LOCATION:
                    sql = sql + " order by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_SHIFT:
                    sql = sql + " order by shift." + PstShift.fieldNames[PstShift.FLD_NAME];
                    break;

                default:
                    sql = sql + " order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            /*
            switch (srcSaleReport.getSortBy()) {
                case SrcSaleReport.SORT_BY_DATE:
                    sql = sql + "\n  order by cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_LOCATION:
                    sql = sql + "\n  order by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_MARK:
                    sql = sql + "\n  order by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_SALES_PERSON:
                    sql = sql + "\n  order by sps." + PstSales.fieldNames[PstSales.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_SHIFT:
                    sql = sql + "\n  order by shift." + PstShift.fieldNames[PstShift.FLD_START_TIME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_SUB_CATEGORY:
                    sql = sql + "\n  order by scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_CATEGORY:
                    //sql = sql + "\n  order by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_SUPPLIER:
                    sql = sql + "\n  order by sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_ITEM:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_TOTAL_QTY:
                    sql = sql + "\n  order by cbd_qty " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_TOTAL_SALE:
                    sql = sql + "\n  order by cbd_total " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_MARGIN:
                    sql = sql + "\n  order by cbd_margin " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_MARGIN_PCT:
                    sql = sql + "\n  order by cbd_margin_pct " + descSort + " ";
                    break;

                default:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
            } */

            //System.out.println("sql data : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                SaleReportItem item = new SaleReportItem();
                item.setItemId(rs.getLong("mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ""));
                item.setItemCode(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ""));
                item.setItemName(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ""));
                // item.setItemQty(rs.getDouble("cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ""));

                item.setTotalQty(rs.getDouble("cbd_qty"));
                item.setTotalSold(rs.getDouble("cbd_total") - rs.getDouble("disc_total"));
                item.setTotalCost(rs.getDouble("cbd_cost"));
                item.setTotalMargin(rs.getDouble("cbd_margin"));
                item.setTotalMarginPct(rs.getDouble("cbd_margin_pct"));

                String matName = "";
                try {
                    Material mats = PstMaterial.fetchExc(item.getItemId());
                    matName = mats.getName();
                    item.setItemName(matName);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (srcSaleReport.getViewColoumChange().size() > 0) {
                    for (int i = 0; i < srcSaleReport.getViewColoumChange().size(); i++) {
                        int coloum = Integer.parseInt((String) srcSaleReport.getViewColoumChange().get(i));
                        switch (coloum) {
                            case SrcSaleReport.SHOW_FIELD_CATEGORY:
                                item.setCategoryId(rs.getLong("cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ""));
                                item.setCategoryName(rs.getString("cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + ""));
                                break;
                            case SrcSaleReport.SHOW_FIELD_MERK:
                                item.setMarkId(rs.getLong("merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + ""));
                                item.setMarkName(rs.getString("merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + ""));
                                break;
                            case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                                item.setSupplierId(rs.getLong("sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + ""));
                                item.setSupplierName(rs.getString("sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + ""));
                                break;
                            case SrcSaleReport.SHOW_FIELD_SELL_PRICE:
                                // item.setCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                                item.setPrice(rs.getDouble("cbd_price"));
                                break;
                            case SrcSaleReport.SHOW_FIELD_BUYING_PRICE:
                                item.setCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                                break;
                            case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                                item.setSalesPersonId(rs.getLong("sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + ""));
                                item.setSalesPersonName(rs.getString("sps." + PstSales.fieldNames[PstSales.FLD_NAME] + ""));
                                if (item.getSalesPersonId() == 0)
                                    item.setSalesPersonName("");

                                break;
                            case SrcSaleReport.SHOW_FIELD_LOCATION:
                                item.setLocationId(rs.getLong("loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ""));
                                item.setLocationName(rs.getString("loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + ""));
                                break;
                            case SrcSaleReport.SHOW_FIELD_SHIFT:
                                item.setShiftId(rs.getLong("shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ""));
                                item.setShiftName(rs.getString("shift." + PstShift.fieldNames[PstShift.FLD_NAME] + ""));
                                if (item.getShiftId() == 0)
                                    item.setShiftName("");

                                break;
                        }
                    }
                }
                result.add(item);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static Vector getListAllView(SrcSaleReport srcSaleReport, int start, int recordToGet, int logMode) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            Shift shift = null;
            String stTime = "";
            String edTime = "";
            Date dtTo = new Date();
            if (srcSaleReport.getShiftId() > 0) {
                /*shift = PstShift.fetchExc(srcSaleReport.getShiftId());
                stTime = Formater.formatDate(shift.getStartTime(), "HH:mm:00");
                edTime = Formater.formatDate(shift.getEndTime(), "HH:mm:00");
                dtTo = new Date(srcSaleReport.getDateFrom().getTime());
                if (shift.getStartTime().getHours() > shift.getEndTime().getHours()) {
                    dtTo.setDate(dtTo.getDate() + 1);
                    //srcSaleReport.setDateTo(dtTo);
                } else {
                    //srcSaleReport.setDateTo(dtTo);
                }*/
                stTime = "00:00:00";
                edTime = "23:59:59";
            } else {
                stTime = "00:00:00";
                edTime = "23:59:59";
            }

            String sql = "";
            sql = "select mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "," +
                    " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "," +
                    " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "," +
                    " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + "," +
                    " sum(cbd.qty) as cbd_qty," +
                    " sum(cbd.cost * cbd.qty) as cbd_cost," +
                    " sum(cbd.total_price) as cbd_total,";

                String startDate = "";
                String endDate = "";
                    if (srcSaleReport.getDateFrom() != null) {
                        startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
                        endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                    }

                    sql = sql + " (cbm.disc/(select sum(dt.qty) from cash_bill_detail as dt inner join cash_bill_main as bm " +
                            " on dt.cash_bill_main_id = bm.cash_bill_main_id "+
                            "where bm.cash_bill_main_id = cbm.cash_bill_main_id " +
                            "and bm.bill_date between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
                    if (srcSaleReport.getLocationId() != 0) {
                        sql = sql + " and bm.location_id = "+srcSaleReport.getLocationId()+") * sum(cbd.qty)) as disc_total,";
                    }else{
                        sql = sql + ") * sum(cbd.qty)) as disc_total,";
                    }

                    sql = sql + " (sum(cbd.total_price) - sum(cbd.cost * cbd.qty)) as cbd_margin," +
                    " ((sum(cbd.total_price) - sum(cbd.cost * cbd.qty)) / sum(cbd.total_price * cbd.qty)* 100 ) as cbd_margin_pct";

            if (srcSaleReport.getViewColoumChange().size() > 0) {
                for (int i = 0; i < srcSaleReport.getViewColoumChange().size(); i++) {
                    int coloum = Integer.parseInt((String) srcSaleReport.getViewColoumChange().get(i));
                    switch (coloum) {
                        case SrcSaleReport.SHOW_FIELD_CATEGORY:
                            sql = sql + ", cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + "," +
                                    " cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_MERK:
                            sql = sql + ", merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "," +
                                    " merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SELL_PRICE:
                            sql = sql + ", (cbd.item_price - cbd.disc) as cbd_price ";
                            break;
                        case SrcSaleReport.SHOW_FIELD_TOTAL_SALE:
                            sql = sql + ", sum(cbd.total_price) as cbd_total ";
                            break;
                        case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                            sql = sql + ", sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "," +
                                    " sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_BUYING_PRICE:
                            sql = sql + ", mat." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                            sql = sql + ", sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "," +
                                    " sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_LOCATION:
                            sql = sql + ", loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + "," +
                                    " loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SHIFT:
                            sql = sql + ", shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "," +
                                    " shift." + PstShift.fieldNames[PstShift.FLD_NAME];
                            break;
                    }
                }
            }

            sql = sql + " FROM cash_bill_main as cbm" +
                    " inner join cash_bill_detail as cbd " +
                    " on cbm.cash_bill_main_id = cbd.cash_bill_main_id" +
                    " inner join pos_material as mat" +
                    " on mat.material_id= cbd.material_id";

            if (srcSaleReport.getViewColoumChange().size() > 0) {
                for (int i = 0; i < srcSaleReport.getViewColoumChange().size(); i++) {
                    int coloum = Integer.parseInt((String) srcSaleReport.getViewColoumChange().get(i));
                    switch (coloum) {
                        case SrcSaleReport.SHOW_FIELD_CATEGORY:
                            sql = sql + " inner join pos_category as cat" +
                                    " on cat.category_id = mat.category_id";
                            break;
                        case SrcSaleReport.SHOW_FIELD_MERK:
                            sql = sql + " inner join pos_merk as merk" +
                                    " on merk.merk_id = mat.merk_id";
                            break;
                        case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                            sql = sql + " inner join pos_vendor_price as vpc" +
                                    " on vpc.material_id= mat.material_id" +
                                    " inner join contact_list as sup" +
                                    " on vpc.vendor_id = sup.contact_id";
                            break;
                        case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                            sql = sql + " left join pos_sales_person as sps" +
                                    " on sps.sales_code = cbm.sales_code";
                            break;
                        case SrcSaleReport.SHOW_FIELD_LOCATION:
                            sql = sql + " inner join location as loc" +
                                    " on loc.location_id = cbm.location_id";
                            break;
                        case SrcSaleReport.SHOW_FIELD_SHIFT:
                            sql = sql + " inner join pos_shift as shift" +
                                    " on cbm.shift_id = shift.shift_id";

                            break;
                    }
                }
            }

            String categoryId = "";
            if (srcSaleReport.getCategoryId() != 0) {
                // categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + srcSaleReport.getCategoryId();
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListCategoryId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListCategoryId().size(); k++) {
                    long oidCateg = Long.parseLong((String) srcSaleReport.getListCategoryId().get(k));
                    if (categoryId.length() > 0) {
                        categoryId = categoryId + " or cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    } else {
                        categoryId = " cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCateg;
                    }
                }
                categoryId = "(" + categoryId + ")";
            }

            String dateTrans = "";
            if (srcSaleReport.getDateFrom() != null) {
                startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
                endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
                dateTrans = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
            }

            String itemId = "";
            if (srcSaleReport.getItemId() > 0) {
                itemId = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + "=" + srcSaleReport.getItemId();
            }

            String itemName = "";
            if (srcSaleReport.getItemName().length() > 0) {
                itemName = " mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + "=" + srcSaleReport.getItemName();
            }

            String locationId = "";
            if (srcSaleReport.getLocationId() != 0) {
                locationId = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
            }

            String markId = "";
            if (srcSaleReport.getMarkId() != 0) {
                markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + srcSaleReport.getMarkId();
            }

            // ini di jalankan jika vector terisi
            if (srcSaleReport.getListMerkId().size() > 0) {
                for (int k = 0; k < srcSaleReport.getListMerkId().size(); k++) {
                    long oidMerk = Long.parseLong((String) srcSaleReport.getListMerkId().get(k));
                    if (markId.length() > 0) {
                        markId = markId + " or merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    } else {
                        markId = " merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + "=" + oidMerk;
                    }
                }
                markId = "(" + markId + ")";
            }

            String salesPersonId = "";
            if (srcSaleReport.getSalesPersonId() != 0) {
                salesPersonId = " sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + "=" + srcSaleReport.getSalesPersonId();
            }

            String shiftId = "";
            if (srcSaleReport.getShiftId() != 0) {
                shiftId = " shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
            }

            String subCategoryId = "";
            if (srcSaleReport.getSubCategoryId() != 0) {
                subCategoryId = " scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + "=" + srcSaleReport.getSubCategoryId();
            }

            String supplierId = "";
            if (srcSaleReport.getSupplierId() != 0) {
                supplierId = " sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + "=" + srcSaleReport.getSupplierId();
            }

            String transType = "";
            if (srcSaleReport.getTransType() >= 0) {
                int[] transMode = getTransMode(srcSaleReport);
                transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + transMode[DOC_TYPE] +
                        //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + transMode[TRANSACTION_TYPE] +
                        " and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_STATUS];
                //" and cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + transMode[TRANSACTION_TYPE]+
            }

            String strBillStatus = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;

            String whereClause = "";
            if (locationId.length() > 0)
                whereClause = locationId;

            if (categoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + categoryId;
                } else {
                    whereClause = whereClause + categoryId;
                }
            }

            if (dateTrans.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + dateTrans;
                } else {
                    whereClause = whereClause + dateTrans;
                }
            }

            if (itemName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemName;
                } else {
                    whereClause = whereClause + itemName;
                }
            }

            if (itemId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + itemId;
                } else {
                    whereClause = whereClause + itemId;
                }
            }

            if (markId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + markId;
                } else {
                    whereClause = whereClause + markId;
                }
            }


            if (salesPersonId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesPersonId;
                } else {
                    whereClause = whereClause + salesPersonId;
                }
            }

            if (shiftId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + shiftId;
                } else {
                    whereClause = whereClause + shiftId;
                }
            }

            if (subCategoryId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + subCategoryId;
                } else {
                    whereClause = whereClause + subCategoryId;
                }
            }

            if (supplierId.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + supplierId;
                } else {
                    whereClause = whereClause + supplierId;
                }
            }

            if (transType.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transType;
                } else {
                    whereClause = whereClause + transType;
                }
            }

            if (strBillStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strBillStatus;
                } else {
                    whereClause = whereClause + strBillStatus;
                }
            }

            if (whereClause.length() > 0) {
                sql = sql + " where " + whereClause; //+" AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
            } else {
                sql = sql + " where cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
            }
            sql = sql + " group by mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+
                    ", cbd.item_price ";

            if (srcSaleReport.getViewColoumChange().size() > 0) {
                for (int i = 0; i < srcSaleReport.getViewColoumChange().size(); i++) {
                    int coloum = Integer.parseInt((String) srcSaleReport.getViewColoumChange().get(i));
                    switch (coloum) {
                        case SrcSaleReport.SHOW_FIELD_CATEGORY:
                            sql = sql + ", cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_MERK:
                            sql = sql + ", merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                            sql = sql + ", sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                            sql = sql + ", sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_LOCATION:
                            sql = sql + ", loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                            break;
                        case SrcSaleReport.SHOW_FIELD_SHIFT:
                            sql = sql + ", shift." + PstShift.fieldNames[PstShift.FLD_NAME];
                            break;
                    }
                }
            }

            String descSort = "";
            if (srcSaleReport.getDescSort() == SrcSaleReport.SORT_DESC) {
                descSort = " DESC ";
            } else {
                descSort = " ASC ";
            }

            switch (srcSaleReport.getSortBy()) {
                case SrcSaleReport.SHOW_FIELD_CODE:
                    sql = sql + " order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    break;
                case SrcSaleReport.SHOW_FIELD_NAME:
                    sql = sql + " order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_BUYING_PRICE:
                    sql = sql + " order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST];
                    break;
                case SrcSaleReport.SHOW_FIELD_QTY:
                    sql = sql + " order by cbd_qty";
                    break;
                case SrcSaleReport.SHOW_FIELD_TOTAL_SALE:
                    sql = sql + " order by cbd_total";
                    break;
                case SrcSaleReport.SHOW_FIELD_SELL_PRICE:
                    sql = sql + " order by cbd_price";
                    break;
                case SrcSaleReport.SHOW_FIELD_CATEGORY:
                    sql = sql + " order by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_MERK:
                    sql = sql + " order by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                    sql = sql + " order by sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                    break;
                case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                    sql = sql + " order by sps." + PstSales.fieldNames[PstSales.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_LOCATION:
                    sql = sql + " order by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME];
                    break;
                case SrcSaleReport.SHOW_FIELD_SHIFT:
                    sql = sql + " order by shift." + PstShift.fieldNames[PstShift.FLD_NAME];
                    break;

                default:
                    sql = sql + " order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU];
            }

            /*
            switch (srcSaleReport.getSortBy()) {
                case SrcSaleReport.SORT_BY_DATE:
                    sql = sql + "\n  order by cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_LOCATION:
                    sql = sql + "\n  order by loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_MARK:
                    sql = sql + "\n  order by merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_SALES_PERSON:
                    sql = sql + "\n  order by sps." + PstSales.fieldNames[PstSales.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_SHIFT:
                    sql = sql + "\n  order by shift." + PstShift.fieldNames[PstShift.FLD_START_TIME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_SUB_CATEGORY:
                    sql = sql + "\n  order by scat." + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_CATEGORY:
                    //sql = sql + "\n  order by cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_SUPPLIER:
                    sql = sql + "\n  order by sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_ITEM:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_TOTAL_QTY:
                    sql = sql + "\n  order by cbd_qty " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_TOTAL_SALE:
                    sql = sql + "\n  order by cbd_total " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_MARGIN:
                    sql = sql + "\n  order by cbd_margin " + descSort + " ";
                    break;

                case SrcSaleReport.SORT_BY_MARGIN_PCT:
                    sql = sql + "\n  order by cbd_margin_pct " + descSort + " ";
                    break;

                default:
                    sql = sql + "\n  order by mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " " + descSort + " ";
            } */

            //System.out.println("sql data : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                SaleReportItem item = new SaleReportItem();
                item.setItemId(rs.getLong("mat." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ""));
                item.setItemCode(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ""));
                item.setItemName(rs.getString("mat." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + ""));
                // item.setItemQty(rs.getDouble("cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ""));

                item.setTotalQty(rs.getDouble("cbd_qty"));
                item.setTotalSold(rs.getDouble("cbd_total") - rs.getDouble("disc_total"));
                item.setTotalCost(rs.getDouble("cbd_cost"));
                item.setTotalMargin(rs.getDouble("cbd_margin"));
                item.setTotalMarginPct(rs.getDouble("cbd_margin_pct"));

                String matName = "";
                try {
                    Material mats = PstMaterial.fetchExc(item.getItemId());
                    matName = mats.getName();
                    item.setItemName(matName);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                if (srcSaleReport.getViewColoumChange().size() > 0) {
                    for (int i = 0; i < srcSaleReport.getViewColoumChange().size(); i++) {
                        int coloum = Integer.parseInt((String) srcSaleReport.getViewColoumChange().get(i));
                        switch (coloum) {
                            case SrcSaleReport.SHOW_FIELD_CATEGORY:
                                item.setCategoryId(rs.getLong("cat." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ""));
                                item.setCategoryName(rs.getString("cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + ""));
                                break;
                            case SrcSaleReport.SHOW_FIELD_MERK:
                                item.setMarkId(rs.getLong("merk." + PstMerk.fieldNames[PstMerk.FLD_MERK_ID] + ""));
                                item.setMarkName(rs.getString("merk." + PstMerk.fieldNames[PstMerk.FLD_NAME] + ""));
                                break;
                            case SrcSaleReport.SHOW_FIELD_SUPPLIER:
                                item.setSupplierId(rs.getLong("sup." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + ""));
                                item.setSupplierName(rs.getString("sup." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + ""));
                                break;
                            case SrcSaleReport.SHOW_FIELD_SELL_PRICE:
                                // item.setCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                                item.setPrice(rs.getDouble("cbd_price"));
                                break;
                            case SrcSaleReport.SHOW_FIELD_BUYING_PRICE:
                                item.setCost(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]));
                                break;
                            case SrcSaleReport.SHOW_FIELD_SALES_NAME:
                                item.setSalesPersonId(rs.getLong("sps." + PstSales.fieldNames[PstSales.FLD_SALES_ID] + ""));
                                item.setSalesPersonName(rs.getString("sps." + PstSales.fieldNames[PstSales.FLD_NAME] + ""));
                                if (item.getSalesPersonId() == 0)
                                    item.setSalesPersonName("");

                                break;
                            case SrcSaleReport.SHOW_FIELD_LOCATION:
                                item.setLocationId(rs.getLong("loc." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + ""));
                                item.setLocationName(rs.getString("loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + ""));
                                break;
                            case SrcSaleReport.SHOW_FIELD_SHIFT:
                                item.setShiftId(rs.getLong("shift." + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + ""));
                                item.setShiftName(rs.getString("shift." + PstShift.fieldNames[PstShift.FLD_NAME] + ""));
                                if (item.getShiftId() == 0)
                                    item.setShiftName("");

                                break;
                        }
                    }
                }
                result.add(item);
            }
            rs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    private static final int DOC_TYPE = 0;
    private static final int TRANSACTION_TYPE = 1;
    private static final int TRANSACTION_STATUS = 2;

    public static int[] getTransMode(SrcSaleReport srcSaleReport) {
        int[] transType = {0, 0, 0};
        switch (srcSaleReport.getTransType()) {
            case SrcReportSale.TYPE_CASH:
                transType[DOC_TYPE] = PstBillMain.TYPE_INVOICE;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CASH;
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_CLOSE;
                break;

            case SrcReportSale.TYPE_RETUR:
                transType[DOC_TYPE] = PstBillMain.TYPE_RETUR;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CASH;
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_CLOSE; 
                break;

            case SrcReportSale.TYPE_CREDIT:
                //jika dipisah dengan invoice
                transType[DOC_TYPE] = PstBillMain.TYPE_INVOICE;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CREDIT; 
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_OPEN;
                break;

            case SrcReportSale.TYPE_OPEN_BILL:
                transType[DOC_TYPE] = PstBillMain.TYPE_INVOICE;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CASH;
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_OPEN;
                break;

            case SrcReportSale.TYPE_GIFT:
                transType[DOC_TYPE] = PstBillMain.TYPE_GIFT;
                transType[TRANSACTION_TYPE] = PstBillMain.TRANS_TYPE_CASH;
                transType[TRANSACTION_STATUS] = PstBillMain.TRANS_STATUS_CLOSE;
                break;
        }
        return transType;
    }
    
    
    public static Vector getListReportVoid(SrcSaleReport srcSaleReport, int start, int recordToGet, int logMode) {
        Vector record = new Vector();
        String stTime = "";
        String edTime = "";
        stTime = "00:00:00";
        edTime = "23:59:59";
        DBResultSet dbrs = null;
        String sql="";
        
        sql = "SELECT cbm.*, cbd."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+", cbd."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" FROM "+PstBillDetailVoid.TBL_CASH_BILL_DETAIL+" AS cbm INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS cbd ON "+
              " cbm."+PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_MAIN_ID]+"= cbd."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];
        
        if (srcSaleReport.getDateFrom() != null) {
            String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
            String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
            sql = sql + " WHERE  cbm." + PstBillDetail.fieldNames[PstBillDetail.FLD_LENGTH_OF_ORDER] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
        }
        
        if (srcSaleReport.getLocationId() != 0) {
             sql = sql + " AND cbd."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"='"+srcSaleReport.getLocationId()+"'";
        }
        
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Billdetail billlDetail  = new Billdetail();
                billlDetail.setItemName(rs.getString(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_NAME]));
                billlDetail.setOID(rs.getLong(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_DETAIL_ID]));
                billlDetail.setQty(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_QUANTITY]));
                billlDetail.setTotalAmount(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_PRICE]));
                billlDetail.setItemPrice(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_PRICE]));
                billlDetail.setNote(rs.getString(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_NOTE]));
                
                billlDetail.setNoBill(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                Date date = DBHandler.convertDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), rs.getTime(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                billlDetail.setBillMainDate(date);
                record.add(billlDetail);
            }
            rs.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        
        return record;
    }
    
    
    public static Vector getListReportError(SrcSaleReport srcSaleReport, int start, int recordToGet, int logMode) {
        Vector record = new Vector();
        String stTime = "";
        String edTime = "";
        stTime = "00:00:00";
        edTime = "23:59:59";
        DBResultSet dbrs = null;
        String sql="";
        
        sql = "SELECT cbm.*, cbd."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+", cbd."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" FROM "+PstBillDetail.TBL_CASH_BILL_DETAIL+" AS cbm INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS cbd ON "+
              " cbm."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"= cbd."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];
        
        if (srcSaleReport.getDateFrom() != null) {
            String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
            String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
            sql = sql + " WHERE  cbm." + PstBillDetail.fieldNames[PstBillDetail.FLD_LENGTH_OF_ORDER] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
        }
        
         sql = sql + " AND cbm."+PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]+"='0' AND cbd.CASH_CASHIER_ID!=1 ";
        
        if (srcSaleReport.getLocationId() != 0) {
             sql = sql + " AND cbd."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"='"+srcSaleReport.getLocationId()+"'";
        }
        
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Billdetail billlDetail  = new Billdetail();
                billlDetail.setItemName(rs.getString(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_NAME]));
                billlDetail.setOID(rs.getLong(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_DETAIL_ID]));
                billlDetail.setQty(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_QUANTITY]));
                billlDetail.setTotalAmount(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_PRICE]));
                billlDetail.setItemPrice(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_PRICE]));
                billlDetail.setNote(rs.getString(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_NOTE]));
                billlDetail.setNoBill(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                Date date = DBHandler.convertDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), rs.getTime(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                billlDetail.setBillMainDate(date);
                record.add(billlDetail);
            }
            rs.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        
        return record;
    }
    
    
    public static Vector getListReportCancel(SrcSaleReport srcSaleReport, int start, int recordToGet, int logMode) {
        
        Vector record = new Vector();
        String stTime = "";
        String edTime = "";
        stTime = "00:00:00";
        edTime = "23:59:59";
        DBResultSet dbrs = null;
        String sql="";
        
        sql = " SELECT cbd.* ,cbm."+PstBillMain.fieldNames[PstBillMain.FLD_NOTES]+""
             +" ,cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+""
             +" ,cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+""   
             +" FROM "+PstBillDetail.TBL_CASH_BILL_DETAIL+" cbd "
             +" INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" cbm "
             +" ON cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"";
        
        if (srcSaleReport.getDateFrom() != null) {
            String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
            String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
            sql = sql + " WHERE  cbd." + PstBillDetail.fieldNames[PstBillDetail.FLD_LENGTH_OF_ORDER] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'";
        }
        
        sql = sql + " AND cbm.DOC_TYPE=0 AND cbm.TRANSACTION_STATUS=2 AND cbm.TRANSACTION_TYPE=0 ";
        
        if (srcSaleReport.getLocationId() != 0) {
             sql = sql + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"='"+srcSaleReport.getLocationId()+"'";
        }
        
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Billdetail billlDetail  = new Billdetail();
                billlDetail.setItemName(rs.getString(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_NAME]));
                billlDetail.setOID(rs.getLong(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_BILL_DETAIL_ID]));
                billlDetail.setQty(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_QUANTITY]));
                billlDetail.setTotalAmount(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_TOTAL_PRICE]));
                billlDetail.setItemPrice(rs.getDouble(PstBillDetailVoid.fieldNames[PstBillDetailVoid.FLD_ITEM_PRICE]));
                billlDetail.setNote(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_NOTES]));
                billlDetail.setNoBill(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                Date date = DBHandler.convertDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]), rs.getTime(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                billlDetail.setBillMainDate(date);
                record.add(billlDetail);
            }
            rs.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        
        return record;
    }
    
    
    public static Vector getListBillDetailCost(SrcSaleReport srcSaleReport) {
        
        Vector record = new Vector();
        String stTime = "";
        String edTime = "";
        stTime = "00:00:00";
        edTime = "23:59:59";
        DBResultSet dbrs = null;
        String sql="";
        
        sql = " SELECT cbd.* ,cbm."+PstBillMain.fieldNames[PstBillMain.FLD_NOTES]+""
             +" ,cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+""
             +" ,pm."+PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]+""
             +" ,pm."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+""   
             +" FROM "+PstBillDetail.TBL_CASH_BILL_DETAIL+" cbd "
             +" INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" cbm "
             +" ON cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+""
             +" INNER JOIN "+PstMaterial.TBL_MATERIAL+" pm "
             +" ON pm."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" = cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"";
        
        if (srcSaleReport.getDateFrom() != null) {
            String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
            String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
            sql = sql + " WHERE  cbm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " between '" + startDate + " " + stTime + "' AND '" + endDate + " " + edTime + "'"
                    +   " AND cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]+"='"+srcSaleReport.getItemId()+"'";
        }
        
        String transType = "cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                              " and (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                              " or cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")" ;
        sql = sql + " AND "+transType;
        
        if (srcSaleReport.getLocationId() != 0) {
             sql = sql + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"='"+srcSaleReport.getLocationId()+"'";
        }
        
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Billdetail billlDetail  = new Billdetail();
                billlDetail.setItemName(rs.getString(PstMaterial.fieldNames[PstMaterial.FLD_NAME]));
                billlDetail.setOID(rs.getLong(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID]));
                billlDetail.setQty(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]));
                billlDetail.setTotalAmount(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]));
                billlDetail.setItemPrice(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_ITEM_PRICE]));
                billlDetail.setCost(rs.getDouble(PstBillDetail.fieldNames[PstBillDetail.FLD_COST]));
                billlDetail.setNote(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_NOTES]));
                billlDetail.setNoBill(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]));
                billlDetail.setItemPriceStock(rs.getDouble(PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE]));
                
                record.add(billlDetail);
            }
            rs.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBResultSet.close(dbrs);
        }
        
        return record;
    }
    
    public static Vector listCashSales(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT "
                    + "DATE_FORMAT(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+", '%Y-%m-%d') AS DATE "
                    + ", DAYNAME(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+") AS DAY"
                    + ", SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] 
                    + " + BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_TAX] + " + (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+")) AS AMOUNT"
                    + ", SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS DISC"
                    + ", SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS HPP"
                    + ", SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] 
                    + " + BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_TAX] 
                    + " - BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+") AS GROSS"
                    + " FROM "
                    + "" + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM"
                    + " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD"
                    + " ON BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+ " AS CP"
                    + " ON CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstMaterial.TBL_MATERIAL+ " AS MAT"
                    + " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + "";
            
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
                sql = sql + " GROUP BY DATE";
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                BillMain billMain = new BillMain();
                
                billMain.setBillDate(rs.getDate("DATE"));
                billMain.setEventName(rs.getString("DAY"));
                billMain.setAmount(rs.getDouble("AMOUNT"));
                billMain.setDiscount(rs.getDouble("DISC"));
                billMain.setServiceValue(rs.getDouble("HPP"));
                billMain.setPaidAmount(rs.getDouble("GROSS"));
                lists.add(billMain);
                
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    
    public static JSONObject listCashSalesLocation(int limitStart, int recordToGet, String whereClause, String order, String inLocation) {
        JSONObject objReturn = new JSONObject();
        JSONArray lists = new JSONArray();
        DBResultSet dbrs = null;
        String whereLocation = "";
        if (inLocation.length()>0){
            whereLocation = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" IN ("+inLocation+")";
        }
        Vector vLocation = PstLocation.list(0, 0, whereLocation, "");
        try {
            String sql = "SELECT "
                    + "DATE_FORMAT(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+", '%Y-%m-%d') AS DATE, ";
                    for (int i=0; i<vLocation.size();i++){
                        Location location = (Location) vLocation.get(i);
                        sql += location.getCode()+".AMOUNT AS "+location.getName().replaceAll(" ", "_");
                        if ((i+1) != vLocation.size()){
                            sql += ",";
                        }
                    }
                   sql += " FROM "
                    + "" + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM"
                    + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+ " AS CP"
                    + " ON CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];
            
                   for (int i=0; i<vLocation.size();i++){
                        Location location = (Location) vLocation.get(i);
                        sql += " LEFT JOIN ("
                            + " SELECT SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] 
                            + " + BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_TAX] + " + (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+"))"
                            + " - SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS AMOUNT,"
                            + " DATE_FORMAT(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+", '%Y-%m-%d') AS DATE FROM "
                            + "" + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM"
                            + " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD"
                            + " ON BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                            + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                            + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+ " AS CP"
                            + " ON CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                            + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                            + " INNER JOIN "+PstMaterial.TBL_MATERIAL+ " AS MAT"
                            + " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                            + " = BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                            + " WHERE "+ whereClause +" AND BM.`LOCATION_ID` = "+location.getOID()+" GROUP BY DATE "
                            + " ) AS "+location.getCode()+" ON "+location.getCode()+".DATE = DATE_FORMAT(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+", '%Y-%m-%d') "
                            + "";
                    }
                   
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
                sql = sql + " GROUP BY DATE";
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                JSONArray arrLoc = new JSONArray();
                JSONObject objLocation = new JSONObject();
                for (int i=0; i<vLocation.size();i++){
                    Location location = (Location) vLocation.get(i);
                    objLocation.put(location.getName().replaceAll(" ", "_"), rs.getString(location.getName().replaceAll(" ", "_")));
                }
                arrLoc.put(objLocation);
                objReturn.put(rs.getString("DATE"), arrLoc);
                
            }
            rs.close();
            return objReturn;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return objReturn;
    }
    
    public static Vector listCashSalesDetail(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT "
                    + "CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]
                    + ", DAYNAME(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+") AS DAY"
                    + ", (BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] 
                    + " + BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_TAX] + " + (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+")) AS AMOUNT"
                    + ", (BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS DISC"
                    + ", (BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS HPP"
                    + ", (BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] 
                    + " + BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_TAX] 
                    + " - BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+") AS GROSS"
                    + ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" AS MAT_NAME"
                    + ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" AS SKU"
                    + ", BM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" AS BILL_NUMBER"
                    + " FROM "
                    + "" + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM"
                    + " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD"
                    + " ON BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+ " AS CP"
                    + " ON CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstMaterial.TBL_MATERIAL+ " AS MAT"
                    + " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + "";
            
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            System.out.println("List Cash Sales Tanggal : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                
                Vector temp = new Vector();
                
                BillMain billMain = new BillMain();
                billMain.setBillDate(rs.getDate(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]));
                billMain.setInvoiceNumber(rs.getString("BILL_NUMBER"));
                billMain.setEventName(rs.getString("DAY"));
                temp.add(billMain);
                
                Billdetail detail = new Billdetail();
                detail.setItemName(rs.getString("MAT_NAME"));
                detail.setSku(rs.getString("SKU"));
                detail.setItemPrice(rs.getDouble("AMOUNT"));
                detail.setCost(rs.getDouble("HPP"));
                detail.setDisc(rs.getDouble("DISC"));
                detail.setTotalAmount(rs.getDouble("GROSS"));
                temp.add(detail);
                lists.add(temp);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static Vector listInvoiceReport(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT "
                    + " BM.* "
                    + " FROM "
                    + "" + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM"
                    + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+ " AS CP"
                    + " ON CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + "";
            
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
                sql = sql + " GROUP BY BM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE];
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            System.out.println("List Cash Sales Tanggal : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                BillMain billMain = new BillMain();
                PstBillMain.resultToObject(rs, billMain);
                lists.add(billMain);
                
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static double listGlobalSale(int limitStart, int recordToGet, String whereClause, String order) {
        double count = 0;
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT "
                    + "SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] 
                    + " + BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_TAX] + ") AS DATA"
                    + " FROM "
                    + "" + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM"
                    + " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD"
                    + " ON BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+ " AS CP"
                    + " ON CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + "";
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            System.out.println("List Count Sales Global Cash : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    public static Vector listCashSalesBulanan(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT "
                    + "CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]
                    + ", MONTHNAME(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+") AS MONTH"
                    + ", SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] 
                    + " + BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_TAX] + " + (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+")) AS AMOUNT"
                    + ", SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS DISC"
                    + ", SUM(BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS HPP"
                    + " FROM "
                    + "" + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM"
                    + " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD"
                    + " ON BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstMaterial.TBL_MATERIAL+ " AS MAT"
                    + " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+ " AS CP"
                    + " ON CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + "";
            
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
                sql = sql + " GROUP BY MONTH(CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+")";
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            System.out.println("List Cash Sales Bulanan : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                BillMain billMain = new BillMain();
                
                billMain.setBillDate(rs.getDate(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]));
                billMain.setEventName(rs.getString("MONTH"));
                billMain.setAmount(rs.getDouble("AMOUNT"));
                billMain.setDiscount(rs.getDouble("DISC"));
                billMain.setServiceValue(rs.getDouble("HPP"));
                lists.add(billMain);
                
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static Vector listCashSalesBulananDetail(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT "
                    + "CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]
                    + ", MONTHNAME(CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+") AS MONTH"
                    + ", (BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] 
                    + " + BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_TAX] + " + (BD." + PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+")) AS AMOUNT"
                    + ", (BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_DISC]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS DISC"
                    + ", (BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_COST]+" * BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+") AS HPP"
                    + ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+" AS MAT_NAME"
                    + ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" AS SKU"
                    + ", BM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" AS BILL_NUMBER"
                    + " FROM "
                    + "" + PstBillMain.TBL_CASH_BILL_MAIN + " AS BM"
                    + " INNER JOIN "+PstBillDetail.TBL_CASH_BILL_DETAIL + " AS BD"
                    + " ON BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " INNER JOIN "+PstMaterial.TBL_MATERIAL+ " AS MAT"
                    + " ON MAT."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]
                    + " = BD."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                    + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+ " AS CP"
                    + " ON CP."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]
                    + " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + "";
            
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            System.out.println("List Cash Sales Bulanan : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                
                Vector temp = new Vector();
                
                BillMain billMain = new BillMain();
                billMain.setBillDate(rs.getDate(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]));
                billMain.setEventName(rs.getString("MONTH"));
                billMain.setInvoiceNumber(rs.getString("BILL_NUMBER"));
                temp.add(billMain);
                
                Billdetail detail = new Billdetail();
                detail.setItemName(rs.getString("MAT_NAME"));
                detail.setSku(rs.getString("SKU"));
                detail.setTotalAmount(rs.getDouble("AMOUNT"));
                detail.setDisc(rs.getDouble("DISC"));
                detail.setCost(rs.getDouble("HPP"));
                temp.add(detail);
                lists.add(temp);
                
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static JSONObject objReportOmzet(String startDate, String endDate, String location){
        JSONObject objData = new JSONObject();
        JSONArray arrData = new JSONArray();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT "
                            + "loc."+PstLocation.fieldNames[PstLocation.FLD_CODE]
                            + ",loc."+PstLocation.fieldNames[PstLocation.FLD_NAME]
                            + ",SUM(cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE]+"+cbd.TOTAL_TAX) AS OMZET "
                            + ", SUM(ROUND(COST * QTY)) AS HPP "
                        + "FROM pos_raditya."+PstBillDetail.TBL_CASH_BILL_DETAIL+" cbd "
                            + "INNER JOIN pos_raditya."+PstBillMain.TBL_CASH_BILL_MAIN+" cbm "
                                + "ON cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"=cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                            + " INNER JOIN pos_raditya."+PstCashPayment.TBL_PAYMENT+" cp "
                                + "ON cp."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+"=cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                            + " INNER JOIN pos_raditya."+PstLocation.TBL_P2_LOCATION+ " loc "
                                + "ON loc."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" = cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]
                            + " INNER JOIN pos_raditya."+PstMaterial.TBL_MATERIAL+" AS mat "
                                + "ON mat."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" = cbd."+PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID]
                            + " WHERE cp."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]+" BETWEEN '"+startDate+" 00:00:00' AND '"+endDate+" 23:59:00' AND cbm.`STATUS` IN (4,5)";
                    if (location.length() > 0 && !location.equals("0")) {
                        sql += " AND cbm." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " IN (" + location+ ")";
                    }
                sql += " GROUP BY loc."+PstLocation.fieldNames[PstLocation.FLD_NAME];
            
                dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                
                JSONObject objOmzet = new JSONObject();
                objOmzet.put(PstLocation.fieldNames[PstLocation.FLD_CODE], rs.getString(PstLocation.fieldNames[PstLocation.FLD_CODE]));
                objOmzet.put(PstLocation.fieldNames[PstLocation.FLD_NAME], rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
                objOmzet.put("OMZET", rs.getDouble("OMZET"));
                objOmzet.put("HPP", rs.getDouble("HPP"));
                arrData.put(objOmzet);
                objData.put("DATA", arrData);

            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return objData;
    }
    
}




















