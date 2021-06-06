package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.*;

public class SrcReportReturn
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
    private int returnSource = -1;
    private long returnTo = 0;
    private int returnReason = -1;
    private int statusDate;
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
    
    public int getReturnSource()
    {
        return returnSource;
    }
    
    public void setReturnSource(int returnSource)
    {
        this.returnSource = returnSource;
    }
    
    public long getReturnTo()
    {
        return returnTo;
    }
    
    public void setReturnTo(long returnTo)
    {
        this.returnTo = returnTo;
    }
    
    public int getReturnReason()
    { 
        return returnReason; 
    }

    public void setReturnReason(int returnReason)
    { 
        this.returnReason = returnReason; 
    }
    
    /**
     * @return the statusDate
     */
    public int getStatusDate() {
        return statusDate;
}

    /**
     * @param statusDate the statusDate to set
     */
    public void setStatusDate(int statusDate) {
        this.statusDate = statusDate;
    }
    
}
