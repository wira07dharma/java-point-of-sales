package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.*;


    /**
     * @param costingId the costingId to set
     */
    //public void setCostingId(long costingId) {
       // this.costingId = costingId;
   // }
public class SrcReportDispatch
{ 

    private long locationId = 0;
    private long supplierId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private Date dateFrom = new Date();
    private Date dateTo = new Date();
    private int sortBy;
    private long dispatchTo = 0;
    
    private int statusDate;
    //add variable costingId
    //private long costingId = 0;

    private Vector costingId = new Vector(1,1);
    
    public long getLocationId()
    {
        return locationId;
    }
    
    public void setLocationId(long locationId)
    {
        this.locationId = locationId;
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
    
    public long getDispatchTo()
    {
        return dispatchTo;
    }
    
    public void setDispatchTo(long dispatchTo)
    {
        this.dispatchTo = dispatchTo;
    }

    /**
     * @return the costingId
     */
    public Vector getCostingId() {
        return costingId;
    }

    /**
     * @param costingId the costingId to set
     */
    public void setCostingId(Vector costingId) {
        this.costingId = costingId;
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

    /**
     * @return the costingId
     */
    //public long getCostingId() {
        //return costingId;
    //}
}
