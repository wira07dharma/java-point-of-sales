package com.dimata.posbo.entity.search;
 
/* package java */ 
import java.util.*;

public class SrcReportSaleGrafikKategori
{ 

    private long locationId = 0;
    private int bulan = 0;
    private int tahun = 1900;
    private int transType =0; 
    
    /** Holds value of property categoryId. */
    private long categoryId;
    
    /** Holds value of property toTahun. */
    private int toTahun;
    
    public long getLocationId()
    {
        return locationId;
    }
    
    public void setLocationId(long locationId)
    {
        this.locationId = locationId;
    }
    
    public int getBulan()
    {
        return bulan;
    }
    
    public void setBulan(int bulan)
    {
        this.bulan = bulan;
    }
    
    public int getTahun()
    {
        return tahun;
    }
    
    public void setTahun(int tahun)
    {
        this.tahun = tahun;
    }
    
    /** Getter for property categoryId.
     * @return Value of property categoryId.
     *
     */
    public long getCategoryId() {
        return this.categoryId;
    }
    
    /** Setter for property categoryId.
     * @param categoryId New value of property categoryId.
     *
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
    
    /** Getter for property toTahun.
     * @return Value of property toTahun.
     *
     */
    public int getToTahun() {
        return this.toTahun;
    }
    
    /** Setter for property toTahun.
     * @param toTahun New value of property toTahun.
     *
     */
    public void setToTahun(int toTahun) {
        this.toTahun = toTahun;
    }
    
    /**
     * Getter for property transType.
     * @return Value of property transType.
     */
    public int getTransType ()
    {
        return transType;
    }
    
    /**
     * Setter for property transType.
     * @param transType New value of property transType.
     */
    public void setTransType (int transType)
    {
        this.transType = transType;
    }
    
}
