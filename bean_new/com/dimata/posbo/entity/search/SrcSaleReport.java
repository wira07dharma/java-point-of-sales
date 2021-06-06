/*
 * SrcSaleReport.java
 *
 * Created on February 19, 2005, 10:56 AM
 */

package com.dimata.posbo.entity.search;

import java.io.*;
import java.util.*;

/**
 * @author wpradnyana
 */
public class SrcSaleReport implements Serializable, Cloneable {
    public static final int MARGIN_BY_COST = 0;
    public static final int MARGIN_BY_SOLD = 1;
    public static final int TYPE_CASH = 0;
    public static final int TYPE_RETUR = 1;
    public static final int TYPE_GIFT = 2;
    public static final int TYPE_PENDING_ORDER_PAYMENT = 3;
    public static final int TYPE_CREDIT = 4;
    public static final int TYPE_OPEN_BILL = 5;

    //cash+credit
    public static final int TYPE_CASH_CREDIT = 6;
    public static final int TYPE_ALL_SALES = 7;
    
    /**
     * status transaksi
     */
    public static final int STATUS_CLOSED = 0;
    public static final int STATUS_OPEN = 1;
    
    public static final String stTransStatus[][] = {
        {"Lunas", "Belum Lunas"},
        {"Closed", "Open"}
    };
    
    public static final int SORT_BY_ITEM = 0;
    public static final int SORT_BY_SALES_PERSON = 1;
    public static final int SORT_BY_SUPPLIER = 2;
    public static final int SORT_BY_CATEGORY = 3;
    public static final int SORT_BY_LOCATION = 4;
    public static final int SORT_BY_SHIFT = 5;
    public static final int SORT_BY_DATE = 6;
    public static final int SORT_BY_MARK = 7;
    public static final int SORT_BY_SUB_CATEGORY = 8;
    public static final int SORT_BY_SUBCATEGORY = 9;
    public static final int SORT_BY_TOTAL_QTY = 10;
    public static final int SORT_BY_TOTAL_SALE = 11;
    public static final int SORT_BY_MARGIN = 12;
    public static final int SORT_BY_MARGIN_PCT = 13;
    
    public static final int SORT_DESC = 0;
    public static final int SORT_ASC = 1;
    
    
    //add opie-eyek 20160803
    //public static final int ORDER_BY_CATEGORY = 0;
    //public static final int ORDER_BY_SUPPLIER = 1;
    //public static final int ORDER_BY_MERK = 2;
    public static String viewOrderBy[][] = {
        {"Item","Sales","Supplier","Category","Location", "Shift","Date","Merk","Sub Category","Total Qty"},
         {"Item","Sales","Supplier","Category","Location", "Shift","Date","Merk","Sub Category","Total Qty"}
        
    };
    
    /**
     * GADNYANA
     * PROCESS FOR VIEWED THE FIELD COLOUM
     */
    public static final int SHOW_FIELD_CODE = 0;
    public static final int SHOW_FIELD_NAME = 1;
    public static final int SHOW_FIELD_QTY = 2;
    public static final int SHOW_FIELD_MERK = 3;
    public static final int SHOW_FIELD_SELL_PRICE = 4;
    public static final int SHOW_FIELD_TOTAL_SALE = 5;
    public static final int SHOW_FIELD_SUPPLIER = 7;
    public static final int SHOW_FIELD_CATEGORY = 6;
    public static final int SHOW_FIELD_BUYING_PRICE = 8;
    public static final int SHOW_FIELD_SALES_NAME = 9;
    public static final int SHOW_FIELD_LOCATION = 10;
    public static final int SHOW_FIELD_SHIFT = 11;
    
    public static String[] showFieldColoum = {"Code", "Name", "Quantity", "Category", "Selling Price",
    "Total Sale", "Group", "Supplier", "Buying Price", "Sales",
    "Location", "Shift"};
    
    
    public static final int GROUP_BY_ITEM = 0;
    public static final int GROUP_BY_SALES = 1;
    public static final int GROUP_BY_SUPPLIER = 2;
    public static final int GROUP_BY_CATEGORY = 3;
    public static final int GROUP_BY_LOCATION = 4;
    public static final int GROUP_BY_SHIFT = 5;
    public static final int GROUP_BY_DATE = 6;
    public static final int GROUP_BY_MARK = 7;
    public static final int GROUP_BY_SUB_CATEGORY = 8;
	public static final int GROUP_BY_STOCK = 9;
    
    
    public static String groupMethod[][] = {
        {"Nama Barang", "Sales", "Supplier", "Kategori", "Lokasi", "Shift", "Tanggal", "Merk", "Sub Kategori", "Stok"},
        {"Item", "Sales", "Supplier", "Category", "Location", "Shift", "Date", "Mark", "Sub Category", "Stock"}
        
    };
    
    //add opie-eyek 20160803
    public static final int VIEW_BY_SUMMARY = 0;
    public static final int VIEW_BY_DETAIL = 1;
    public static String viewMethod[][] = {
        {"Summary","Detail"},
        {"Summary","Detail"}
        
    };
    
    
    
    public static String reportType[][] = {
        {"Penjualan Cash", "Retur", "Hadiah", "Pembayaran Pending Order", "Penjualan Kredit", "Open Bill","Penjualan Cash dan Kredit","Semua Penjualan"},
        {"Cash Sales", "Return", "Gift", "Pending Order Payment", "Credit Sales", "Open Bill","Cash and Credit Sales"," All Sales"}
    };
    
    public static int FLD_CATEGORY = 0;
    public static int FLD_DATE_FROM = 1;
    public static int FLD_DATE_TO = 2;
    public static int FLD_GROUP_BY = 3;
    public static int FLD_ITEM = 4;
    public static int FLD_LOCATION = 5;
    public static int FLD_MERK = 6;
    public static int FLD_SALES = 7;
    public static int FLD_SHIFT = 8;
    public static int FLD_SORT_BY = 9;
    public static int FLD_SUB_CATEGORY = 10;
    public static int FLD_TRANS_TYPE = 11;
    
    public static String fieldNames[][] = {
        {"KATEGORI", "DARI TANGGAL", "SAMPAI TANGGAL", "PER", "PRODUK", "LOKASI", "MERK", "SALES", "SHIFT", "URUT BERDASARKAN", "SUB KATEGORI", "JENIS TRANSAKSI"},
        {"CATEGORY", "FROM DATE", "TO DATE", "GROUPED BY", "PRODUCT", "LOCATION", "MERK", "SALES PERSON", "SHIFT", "SORT BY", "SUB CATEGORY", "TRANSACTION TYPE"}
    };
    
    public int getQueryType() {
        return queryType;
    }
    
    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }
    
    private int queryType = GROUP_BY_CATEGORY;
    
    /**
     * Holds value of property useDate
     * 0 : not used, 1 : used
     */
    private int useDate = 0;
    
    /**
     * Holds value of property dateFrom.
     */
    private Date dateFrom = new Date();
    
    /**
     * Holds value of property dateTo.
     */
    private Date dateTo = new Date();
    
    /**
     * Holds value of property shiftId.
     */
    private long shiftId = 0;
    
    /**
     * Holds value of property categoryId.
     */
    private long categoryId = 0;
    
    /**
     * Holds value of property subCategoryId.
     */
    private long subCategoryId = 0;
    
    /**
     * Holds value of property itemName.
     */
    private String itemName = "";
    
    /**
     * Holds value of property markId.
     */
    private long markId = 0;
    
    /**
     * Holds value of property locationId.
     */
    private long locationId = 0;
    
    /**
     * Holds value of property supplierId.
     */
    private long supplierId = 0;
    
    /**
     * Holds value of property salesPersonId.
     */
    private long salesPersonId = 0;
    
    /**
     * Holds value of property transType.
     */
    private int transType = 0;
    
    /**
     * Holds value of property sortBy.
     */
    private int sortBy = 0;
    
    /**
     * Holds value of property groupBy.
     */
    private int groupBy = 0;
    
    /**
     * Holds value of property itemId.
     */
    private long itemId = 0;
    
    /**
     * Holds value of property detailGroup.
     */
    private int detailGroup = 0;
    
    /**
     * Holds value of property descSort.
     */
    private int descSort;
    
    /**
     * Holds value of property currencyOid.
     */
    private long currencyOid;
    
    /**
     * Holds value of property custName
     */
    private String custName = "";
    
    /**
     * Holds value of property custCode
     */
    private String custCode = "";
    
    /**
     * Holds value of property transStatus
     */
    private int transStatus = 0;
    
    private int imageView = 0;

    //cashMaster id
    private long cashMasterId = 0;

    //cashCashier id
    private long cashCashierId = 0;
    
    
    private Vector status = new Vector();
    
    private String invoiceNumber = "";
     
    private String note = "";
    
    //add opie-eyek 20160803
    private int viewReport=0;
    private int orderby=0;
    private int viewStock=0;
    private int billDateStatus=0;
    
    public int getImageView() {
        return imageView;
    }
    
    public void setImageView(int imageView) {
        this.imageView = imageView;
    }
    
    private Vector viewColoum = new Vector();
    
    public Vector getViewColoumChange() {
        return viewColoum;
    }
    
    public void setViewColoum(int coloum) {
        switch (coloum) {
            //case SHOW_FIELD_CODE:
            //    viewColoum.add(String.valueOf(SHOW_FIELD_CODE));
            //    break;
            //case SHOW_FIELD_NAME:
            //    viewColoum.add(String.valueOf(SHOW_FIELD_NAME));
            //    break;
            case SHOW_FIELD_CATEGORY:
                viewColoum.add(String.valueOf(SHOW_FIELD_CATEGORY));
                break;
            case SHOW_FIELD_MERK:
                viewColoum.add(String.valueOf(SHOW_FIELD_MERK));
                break;
            case SHOW_FIELD_SELL_PRICE:
                viewColoum.add(String.valueOf(SHOW_FIELD_SELL_PRICE));
                break;
            case SHOW_FIELD_TOTAL_SALE:
                viewColoum.add(String.valueOf(SHOW_FIELD_TOTAL_SALE));
                break;
                //case SHOW_FIELD_QTY:
                //    viewColoum.add(String.valueOf(SHOW_FIELD_QTY));
                //    break;
            case SHOW_FIELD_SUPPLIER:
                viewColoum.add(String.valueOf(SHOW_FIELD_SUPPLIER));
                break;
            case SHOW_FIELD_BUYING_PRICE:
                viewColoum.add(String.valueOf(SHOW_FIELD_BUYING_PRICE));
                break;
            case SHOW_FIELD_SALES_NAME:
                viewColoum.add(String.valueOf(SHOW_FIELD_SALES_NAME));
                break;
            case SHOW_FIELD_LOCATION:
                viewColoum.add(String.valueOf(SHOW_FIELD_LOCATION));
                break;
            case SHOW_FIELD_SHIFT:
                viewColoum.add(String.valueOf(SHOW_FIELD_SHIFT));
                break;
        }
    }
    
    
    private Vector groupColoum = new Vector();
    
    public Vector getGroupColoum() {
        return groupColoum;
    }
    
    public void setGroupColoum(int grpColoum) {
        if (groupColoum == null)
            groupColoum = new Vector(1, 1);
        
        switch (grpColoum) {
            case GROUP_BY_ITEM:
                groupColoum.add(String.valueOf(GROUP_BY_ITEM));
                break;
            case GROUP_BY_SALES:
                groupColoum.add(String.valueOf(GROUP_BY_SALES));
                break;
            case GROUP_BY_SUPPLIER:
                groupColoum.add(String.valueOf(GROUP_BY_SUPPLIER));
                break;
            case GROUP_BY_CATEGORY:
                groupColoum.add(String.valueOf(GROUP_BY_CATEGORY));
                break;
            case GROUP_BY_LOCATION:
                groupColoum.add(String.valueOf(GROUP_BY_LOCATION));
                break;
            case GROUP_BY_SHIFT:
                groupColoum.add(String.valueOf(GROUP_BY_SHIFT));
                break;
            case GROUP_BY_DATE:
                groupColoum.add(String.valueOf(GROUP_BY_DATE));
                break;
            case GROUP_BY_MARK:
                groupColoum.add(String.valueOf(GROUP_BY_MARK));
                break;
        }
    }
    
    /**
     * Creates a new instance of SrcSaleReport
     */
    public SrcSaleReport() {
    }
    
    /**
     * Getter for property dateFrom.
     *
     * @return Value of property dateFrom.
     */
    public Date getDateFrom() {
        return this.dateFrom;
    }
    
    /**
     * Setter for property dateFrom.
     *
     * @param dateFrom New value of property dateFrom.
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    
    /**
     * Getter for property dateTo.
     *
     * @return Value of property dateTo.
     */
    public Date getDateTo() {
        return this.dateTo;
    }
    
    /**
     * Setter for property dateTo.
     *
     * @param dateTo New value of property dateTo.
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    
    /**
     * Getter for property shiftId.
     *
     * @return Value of property shiftId.
     */
    public long getShiftId() {
        return this.shiftId;
    }
    
    /**
     * Setter for property shiftId.
     *
     * @param shiftId New value of property shiftId.
     */
    public void setShiftId(long shiftId) {
        this.shiftId = shiftId;
    }
    
    /**
     * Getter for property categoryId.
     *
     * @return Value of property categoryId.
     */
    public long getCategoryId() {
        return this.categoryId;
    }
    
    /**
     * Setter for property categoryId.
     *
     * @param categoryId New value of property categoryId.
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * gadnyana
     * ini di pakai untuk data
     * category yang memakai check box
     */
    private Vector lsvCateg = new Vector();
    
    public void setCategoryId(String oidCateg) {
        lsvCateg.add(String.valueOf(oidCateg));
    }
    
    public Vector getListCategoryId() {
        return lsvCateg;
    }
    
    /**
     * gadnyana
     * merk ini di pakai untuk data
     * merk dengan pilihan dengan check box
     */
    private Vector lsvMerk = new Vector();
    
    public void setMerkId(String oidMerk) {
        lsvMerk.add(String.valueOf(oidMerk));
    }
    
    public Vector getListMerkId() {
        return lsvMerk;
    }
    
    public void resetVector() {
        lsvMerk = new Vector();
        lsvCateg = new Vector();
    }
    
    /**
     * Getter for property subCategoryId.
     *
     * @return Value of property subCategoryId.
     */
    public long getSubCategoryId() {
        return this.subCategoryId;
    }
    
    /**
     * Setter for property subCategoryId.
     *
     * @param subCategoryId New value of property subCategoryId.
     */
    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
    
    /**
     * Getter for property itemName.
     *
     * @return Value of property itemName.
     */
    public String getItemName() {
        return this.itemName;
    }
    
    /**
     * Setter for property itemName.
     *
     * @param itemName New value of property itemName.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    /**
     * Getter for property markId.
     *
     * @return Value of property markId.
     */
    public long getMarkId() {
        return this.markId;
    }
    
    /**
     * Setter for property markId.
     *
     * @param markId New value of property markId.
     */
    public void setMarkId(long markId) {
        this.markId = markId;
    }
    
    /**
     * Getter for property locationId.
     *
     * @return Value of property locationId.
     */
    public long getLocationId() {
        return this.locationId;
    }
    
    /**
     * Setter for property locationId.
     *
     * @param locationId New value of property locationId.
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    /**
     * Getter for property supplierId.
     *
     * @return Value of property supplierId.
     */
    public long getSupplierId() {
        return this.supplierId;
    }
    
    /**
     * Setter for property supplierId.
     *
     * @param supplierId New value of property supplierId.
     */
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }
    
    /**
     * Getter for property salesPersonId.
     *
     * @return Value of property salesPersonId.
     */
    public long getSalesPersonId() {
        return this.salesPersonId;
    }
    
    /**
     * Setter for property salesPersonId.
     *
     * @param salesPersonId New value of property salesPersonId.
     */
    public void setSalesPersonId(long salesPersonId) {
        this.salesPersonId = salesPersonId;
    }
    
    /**
     * Getter for property transType.
     *
     * @return Value of property transType.
     */
    public int getTransType() {
        return this.transType;
    }
    
    /**
     * Setter for property transType.
     *
     * @param transType New value of property transType.
     */
    public void setTransType(int transType) {
        this.transType = transType;
    }
    
    /**
     * Getter for property sortBy.
     *
     * @return Value of property sortBy.
     */
    public int getSortBy() {
        return this.sortBy;
    }
    
    /**
     * Setter for property sortBy.
     */
    private Vector sortByColoum = new Vector();
    
    public Vector getSortByColoum() {
        return sortByColoum;
    }
    
    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        /*if (sortByColoum == null)
            sortByColoum = new Vector(1, 1);
        switch (sortBy) {
            case SHOW_FIELD_CODE:
                viewColoum.add(String.valueOf(SHOW_FIELD_CODE));
                break;
            case SHOW_FIELD_NAME:
                viewColoum.add(String.valueOf(SHOW_FIELD_NAME));
                break;
            case SHOW_FIELD_CATEGORY:
                viewColoum.add(String.valueOf(SHOW_FIELD_CATEGORY));
                break;
            case SHOW_FIELD_MERK:
                viewColoum.add(String.valueOf(SHOW_FIELD_MERK));
                break;
            case SHOW_FIELD_SELL_PRICE:
                viewColoum.add(String.valueOf(SHOW_FIELD_SELL_PRICE));
                break;
            case SHOW_FIELD_TOTAL_SALE:
                viewColoum.add(String.valueOf(SHOW_FIELD_TOTAL_SALE));
                break;
            case SHOW_FIELD_QTY:
                viewColoum.add(String.valueOf(SHOW_FIELD_QTY));
                break;
            case SHOW_FIELD_SUPPLIER:
                viewColoum.add(String.valueOf(SHOW_FIELD_SUPPLIER));
                break;
            case SHOW_FIELD_BUYING_PRICE:
                viewColoum.add(String.valueOf(SHOW_FIELD_BUYING_PRICE));
                break;
            case SHOW_FIELD_SALES_NAME:
                viewColoum.add(String.valueOf(SHOW_FIELD_SALES_NAME));
                break;
            case SHOW_FIELD_LOCATION:
                viewColoum.add(String.valueOf(SHOW_FIELD_LOCATION));
                break;
            case SHOW_FIELD_SHIFT:
                viewColoum.add(String.valueOf(SHOW_FIELD_SHIFT));
                break;
        }*/
    }
    
    
    /**
     * Getter for property groupBy.
     *
     * @return Value of property groupBy.
     */
    public int getGroupBy() {
        return groupBy;
    }
    
    /**
     * Setter for property groupBy.
     *
     * @param groupBy New value of property groupBy.
     */
    public void setGroupBy(int groupBy) {
        this.groupBy = groupBy;
    }
    
    public void setSortValuesFromParent(int groupMeth, long oidGroup) {
        //int groupMeth = srcSaleReport.getGroupBy ();
        //System.out.println("before tranform "+srcSaleReport.showValues ());
        //this.setGroupBy (groupMeth);
        //SrcSaleReport newSaleReport = new SrcSaleReport();
        //this.getClass () = srcSaleReport;
        
        switch (groupMeth) {
            case SrcSaleReport.GROUP_BY_CATEGORY:
                setCategoryId(oidGroup);
                //this.setDetailGroup (SrcSaleReport.GROUP_BY_ITEM);
                break;
            case SrcSaleReport.GROUP_BY_LOCATION:
                setLocationId(oidGroup);
                //linkParameter = String.valueOf(srcSaleReport.getLocationId());
                break;
            case SrcSaleReport.GROUP_BY_SALES:
                setSalesPersonId(oidGroup);
                //linkParameter = String.valueOf(srcSaleReport.getSalesPersonId());
                break;
            case SrcSaleReport.GROUP_BY_SHIFT:
                setShiftId(oidGroup);
                //linkParameter = String.valueOf(srcSaleReport.getShiftId());
                break;
            case SrcSaleReport.GROUP_BY_SUPPLIER:
                setSupplierId(oidGroup);
                //linkParameter = String.valueOf(srcSaleReport.getSupplierId());
                break;
            case SrcSaleReport.GROUP_BY_ITEM:
                //srcSaleReport.setItemId(oidGroup);
                //linkParameter = String.valueOf(srcSaleReport.getItemId());
                break;
            default:
                //srcSaleReport.setItemId(oidGroup);
                //linkParameter = String.valueOf(srcSaleReport.getItemId());
                break;
        }
        //System.out.println("after tranformed"+srcSaleReport.showValues ());
        // return srcSaleReport;
    }
    
    /**
     * Getter for property itemId.
     *
     * @return Value of property itemId.
     */
    public long getItemId() {
        return this.itemId;
    }
    
    /**
     * Setter for property itemId.
     *
     * @param itemId New value of property itemId.
     */
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
    
    /**
     * Getter for property parentSort.
     *
     * @return Value of property parentSort.
     */
    public int getDetailGroup() {
        return this.detailGroup;
    }
    
    /**
     * Setter for property parentSort.
     *
     * @param parentSort New value of property parentSort.
     */
    public void setDetailGroup(int detailGroup) {
        this.detailGroup = detailGroup;
    }
    
    
    public String showValues() {
        StringBuffer buff = new StringBuffer();
        buff.append("\n Category " + this.getCategoryId());
        //buff.append ("\n "+this.getCategoryName ());
        buff.append("\n Date From " + this.getDateFrom().toString());
        buff.append("\n Date to " + this.getDateTo().toString());
        buff.append("\n Item id " + this.getItemId());
        buff.append("\n Item name " + this.getItemName());
        //buff.append ("\n "+this.getItemQty ());
        buff.append("\n Location id " + this.getLocationId());
        //buff.append ("\n "+this.getLocationName ());
        buff.append("\n Mark id " + this.getMarkId());
        //buff.append ("\n "+this.getMarkName () );
        buff.append("\n Sales id " + this.getSalesPersonId());
        //buff.append ("\n "+this.getSalesPersonName ());
        buff.append("\n Shift id " + this.getShiftId());
        //buff.append ("\n "+this.getShiftName () );
        buff.append("\n Sub Category id " + this.getSubCategoryId());
        //buff.append ("\n "+this.getSubCategoryName () );
        buff.append("\n Supplier id " + this.getSupplierId());
        //buff.append ("\n "+this.getSupplierName ());
        //buff.append ("\n "+this.getTotalQty ());
        //buff.append ("\n "+this.getTotalSold ());
        buff.append("\n Trans type " + this.getTransType());
        
        buff.append("\n Group by " + this.getGroupBy());
        buff.append("\n Detail group " + this.getDetailGroup());
        String values = new String(buff);
        return values;
    }
    
    /**
     * Getter for property descSort.
     *
     * @return Value of property descSort.
     */
    public int getDescSort() {
        return this.descSort;
    }
    
    /**
     * Setter for property descSort.
     *
     * @param descSort New value of property descSort.
     */
    public void setDescSort(int descSort) {
        this.descSort = descSort;
    }
    
    /**
     * Getter for property currencyOid.
     *
     * @return Value of property currencyOid.
     */
    public long getCurrencyOid() {
        return this.currencyOid;
    }
    
    /**
     * Setter for property currencyOid.
     *
     * @param currencyOid New value of property currencyOid.
     */
    public void setCurrencyOid(long currencyOid) {
        this.currencyOid = currencyOid;
    }
    
    /**
     * Getter for property custName.
     *
     * @return Value of property custName.
     */
    public java.lang.String getCustName() {
        return custName;
    }
    
    /**
     * Setter for property custName.
     *
     * @param custName New value of property custName.
     */
    public void setCustName(java.lang.String custName) {
        this.custName = custName;
    }
    
    /**
     * Getter for property custCode.
     *
     * @return Value of property custCode.
     */
    public java.lang.String getCustCode() {
        return custCode;
    }
    
    /**
     * Setter for property custCode.
     *
     * @param custCode New value of property custCode.
     */
    public void setCustCode(java.lang.String custCode) {
        this.custCode = custCode;
    }
    
    /**
     * Getter for property transStatus.
     *
     * @return Value of property transStatus.
     */
    public int getTransStatus() {
        return transStatus;
    }
    
    /**
     * Setter for property transStatus.
     *
     * @param transStatus New value of property transStatus.
     */
    public void setTransStatus(int transStatus) {
        this.transStatus = transStatus;
    }
    
    /**
     * Getter for property useDate.
     *
     * @return Value of property useDate.
     */
    public int getUseDate() {
        return useDate;
    }
    
    /**
     * Setter for property useDate.
     *
     * @param useDate New value of property useDate.
     */
    public void setUseDate(int useDate) {
        this.useDate = useDate;
    }

    /**
     * @return the cashMasterId
     */
    public long getCashMasterId() {
        return cashMasterId;
    }

    /**
     * @param cashierId the cashierId to set
     */
    public void setCashMasterId(long cashMasterId) {
        this.cashMasterId = cashMasterId;
    }

    /**
     * @return the cashCashierId
     */
    public long getCashCashierId() {
        return cashCashierId;
    }

    /**
     * @param cashCashierId the cashCashierId to set
     */
    public void setCashCashierId(long cashCashierId) {
        this.cashCashierId = cashCashierId;
    }
    
    /**
     * @return the status
     */
    public Vector getStatus() {
        return status;
}

    /**
     * @param status the status to set
     */
    public void setStatus(Vector status) {
        this.status = status;
    }

    /**
     * @return the invoiceNumber
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /**
     * @param invoiceNumber the invoiceNumber to set
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the viewReport
     */
    public int getViewReport() {
        return viewReport;
    }

    /**
     * @param viewReport the viewReport to set
     */
    public void setViewReport(int viewReport) {
        this.viewReport = viewReport;
    }

    /**
     * @return the orderby
     */
    public int getOrderby() {
        return orderby;
    }

    /**
     * @param orderby the orderby to set
     */
    public void setOrderby(int orderby) {
        this.orderby = orderby;
    }

    /**
     * @return the viewStock
     */
    public int getViewStock() {
        return viewStock;
    }

    /**
     * @param viewStock the viewStock to set
     */
    public void setViewStock(int viewStock) {
        this.viewStock = viewStock;
    }

    /**
     * @return the billDateStatus
     */
    public int getBillDateStatus() {
        return billDateStatus;
    }

    /**
     * @param billDateStatus the billDateStatus to set
     */
    public void setBillDateStatus(int billDateStatus) {
        this.billDateStatus = billDateStatus;
    }
    
}
