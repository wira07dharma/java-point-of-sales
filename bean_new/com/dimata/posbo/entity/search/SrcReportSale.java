package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.*;

public class SrcReportSale
{ 

    private long locationId = 0;
    private long shiftId = 0;
    private long operatorId = 0;
    private long supplierId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private Date dateFrom = new Date();
    private Date dateTo = new Date();
    private int sortBy;
    private String salesCode = "";
    private long currencyId = 0;
    private int supplierType = 0;
    private int docType=0; 
    private int transStatus = 0;
    //tambah var cashierId
    private long cashMasterId = 0;
    private String categoryMultiple ="";
    private String locationMultiple ="";
    
    public static final int TYPE_CASH=0;
    public static final int TYPE_RETUR=1;
    public static final int TYPE_GIFT=2;
    public static final int TYPE_PENDING_ORDER_PAYMENT=3;    
    public static final int TYPE_CREDIT=4;
    public static final int TYPE_OPEN_BILL=5;

    //cash + credit
    public static final int TYPE_CASH_CREDIT=6;
    
    
    public static String reportType[][]={
        {"TUNAI","RETUR","HADIAH","PEMBAYARAN PENDING ORDER","KREDIT","OPEN BILL"},
        {"CASH","RETURN","GIFT","PENDING ORDER PAYMENT","CREDIT SALE","OPEN BILL"}
    };
    public long getLocationId()
    {
        return locationId;
    }
    
    public void setLocationId(long locationId)
    {
        this.locationId = locationId;
    }
    
    public long getShiftId()
    {
        return shiftId;
    }
    
    public void setShiftId(long shiftId)
    {
        this.shiftId = shiftId;
    }
    
    public long getOperatorId()
    {
        return operatorId;
    }
    
    public void setOperatorId(long operatorId)
    {
        this.operatorId = operatorId;
    }
    
    public long getSupplierId()
    {
        return supplierId;
    }
    
    public void setSupplierId(long supplierId)
    {
        this.supplierId = supplierId;
    }
    
    public long getCategoryId()
    {
        return categoryId;
    }
    
    public void setCategoryId(long categoryId)
    {
        this.categoryId = categoryId;
    }
    
    public long getSubCategoryId()
    {
        return subCategoryId;
    }
    
    public void setSubCategoryId(long subCategoryId)
    {
        this.subCategoryId = subCategoryId;
    }
    
    public Date getDateFrom()
    {
        return dateFrom;
    }
    
    public void setDateFrom(Date dateFrom)
    {
        this.dateFrom = dateFrom;
    }
    
    public Date getDateTo()
    {
        return dateTo;
    }
    
    public void setDateTo(Date dateTo)
    {
        this.dateTo = dateTo;
    }
    
    public int getSortBy()
    {
        return sortBy;
    }
    
    public void setSortBy(int sortBy)
    {
        this.sortBy = sortBy;
    }
    
    public String getSalesCode()
    {
        return salesCode;
    }
    
    public void setSalesCode(String salesCode)
    {
        if ((salesCode == null) || (salesCode.length() == 0))
        {
            salesCode = "";
        }
        this.salesCode = salesCode;
    }
    
    public long getCurrencyId()
    {
        return currencyId;
    }
    
    public void setCurrencyId(long currencyId)
    {
        this.currencyId = currencyId;
    }
    
    public int getSupplierType()
    {
        return supplierType;
    }
    
    public void setSupplierType(int supplierType)
    {
        this.supplierType = supplierType;
    }    
    
    /**
     * Getter for property docType.
     * @return Value of property docType.
     */
    public int getDocType ()
    {
        return docType;
    }
    
    /**
     * Setter for property docType.
     * @param docType New value of property docType.
     */
    public void setDocType (int docType)
    {
        this.docType = docType;
    }
    
    /**
     * Getter for property transStatus.
     * @return Value of property transStatus.
     */
    public int getTransStatus ()
    {
        return transStatus;
    }
    
    /**
     * Setter for property transStatus.
     * @param transStatus New value of property transStatus.
     */
    public void setTransStatus (int transStatus)
    {
        this.transStatus = transStatus;
    }

    /**
     * @return the cashMasterId
     */
    public long getCashMasterId() {
        return cashMasterId;
    }

    /**
     * @param cashMasterId the cashMasterId to set
     */
    public void setCashMasterId(long cashMasterId) {
        this.cashMasterId = cashMasterId;
    }

    /**
     * @return the categoryMultiple
     */
    public String getCategoryMultiple() {
        return categoryMultiple;
    }

    /**
     * @param categoryMultiple the categoryMultiple to set
     */
    public void setCategoryMultiple(String categoryMultiple) {
        this.categoryMultiple = categoryMultiple;
    }

    /**
     * @return the locationMultiple
     */
    public String getLocationMultiple() {
        return locationMultiple;
    }

    /**
     * @param locationMultiple the locationMultiple to set
     */
    public void setLocationMultiple(String locationMultiple) {
        this.locationMultiple = locationMultiple;
    }
    
}
