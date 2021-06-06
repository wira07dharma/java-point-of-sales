package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.*;

public class SrcReportStockOpname
{ 

    private long locationId = 0;
    private long supplierId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private Date dateFrom = new Date();
    private Date dateTo = new Date();
    private int sortBy;
    
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
    
}
