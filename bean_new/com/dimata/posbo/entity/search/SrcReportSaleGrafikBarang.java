package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.*;

public class SrcReportSaleGrafikBarang
{ 

    private long locationId = 0;
    private long materialId = 0;
    private String sku = "";
    private int tahun = 0;
	
    public long getLocationId()
    {
        return locationId;
    }
    
    public void setLocationId(long locationId)
    {
        this.locationId = locationId;
    }
    
    public long getMaterialId()
    {
        return materialId;
    }
    
    public void setMaterialId(long materialId)
    {
        this.materialId = materialId;
    }
    
    public String getSku()
    {
        return sku;
    }
    
    public void setSku(String sku)
    {
        if (sku == null) sku = "";
        this.sku = sku;
    }
    
    public int getTahun()
    {
        return tahun;
    }
    
    public void setTahun(int tahun)
    {
        this.tahun = tahun;
    }
    
}
