package com.dimata.posbo.entity.search;

/* package java */
import java.util.*;

public class SrcReportReceive {
    
    private long locationId = 0;
    private long shiftId = 0;
    private long operatorId = 0;
    private long supplierId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private Date dateFrom = new Date();
    private Date dateTo = new Date();
    private int statusdate;
    private int sortBy;
    private int receiveSource = -1;
    private long receiveFrom = 0;
    private long currencyId = 0;
    
    private long priceTypeId=0;
    private int groupBy=0;
    private String multiPriceType="";
    private String multiLocation ="";
    
    public long getLocationId() {
        return locationId;
    }
    
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    public long getShiftId() {
        return shiftId;
    }
    
    public void setShiftId(long shiftId) {
        this.shiftId = shiftId;
    }
    
    public long getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }
    
    public long getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }
    
    public long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    
    public long getSubCategoryId() {
        return subCategoryId;
    }
    
    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
    
    public Date getDateFrom() {
        return dateFrom;
    }
    
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    
    public Date getDateTo() {
        return dateTo;
    }
    
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
    
    public int getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }
    
    public int getReceiveSource() {
        return receiveSource;
    }
    
    public void setReceiveSource(int receiveSource) {
        this.receiveSource = receiveSource;
    }
    
    public long getReceiveFrom() {
        return receiveFrom;
    }
    
    public void setReceiveFrom(long receiveFrom) {
        this.receiveFrom = receiveFrom;
    }
    
    public long getCurrencyId() {
        return currencyId;
    }
    
    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }
    
    /**
     * @return the statusdate
     */
    public int getStatusdate() {
        return statusdate;
}

    /**
     * @param statusdate the statusdate to set
     */
    public void setStatusdate(int statusdate) {
        this.statusdate = statusdate;
    }

    /**
     * @return the priceTypeId
     */
    public long getPriceTypeId() {
        return priceTypeId;
    }

    /**
     * @param priceTypeId the priceTypeId to set
     */
    public void setPriceTypeId(long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    /**
     * @return the groupBy
     */
    public int getGroupBy() {
        return groupBy;
    }

    /**
     * @param groupBy the groupBy to set
     */
    public void setGroupBy(int groupBy) {
        this.groupBy = groupBy;
    }

    /**
     * @return the multiLocation
     */
    public String getPriceType() {
        return multiPriceType;
    }

    /**
     * @param multiLocation the multiLocation to set
     */
    public void setMultiPriceType(String multiPriceType) {
        this.multiPriceType = multiPriceType;
    }

    /**
     * @return the multiLocation
     */
    public String getMultiLocation() {
        return multiLocation;
    }

    /**
     * @param multiLocation the multiLocation to set
     */
    public void setMultiLocation(String multiLocation) {
        this.multiLocation = multiLocation;
    }
    
}
