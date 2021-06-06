package com.dimata.posbo.entity.search;

/* package java */
import java.util.*;

public class SrcReportStock {
    
    // konstanta untuk menentukkan apakah nilai stok meupakan hasil kali dengan HPP dari master atau HPP dari transaksi
    public static final int STOCK_VALUE_BY_COGS_MASTER = 0;
    public static final int STOCK_VALUE_BY_COGS_TRANSACTION = 1;
    
    public static String stringStockValueBy[][] = {
        {"HPP Master","HPP Transaksi"},
        {"COGS Master","COGS Transaction"}
    };
    
    public static final int SHOW_QTY_ONLY = 0;
    public static final int SHOW_VALUE_ONLY = 1;
    
    public static String stringInfoShowed[][] = {
        {"Jumlah Stok","Nilai Stok"},
        {"Quantity Stock","Stock alue"}
    };
    
    private long locationId = 0;
    private long supplierId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private long periodeId = 0;
    private String sku = "";
    private Date dateFrom = new Date();
    private Date dateTo = new Date();
    private int type;
    private long merkId = 0;
    private int stockValueBy = 0;
    private int infoShowed = 0; //variable ini digunakan untuk menentukkan tipe informasi yang akan ditampilkan
    private long userId = 0; //variabel ini untuk membedakan antara laporan berdasarkan user yang men-generate-nya
    private boolean generateReport = false; //variabel ini untuk menentukkan apakah laporan perlu di-generate ulang
    private String materialName = "";
    private String ksg = "";
    private int sortBy;
    
    public String getKsg() {
        return ksg;
    }

    public void setKsg(String ksg) {
        this.ksg = ksg;
    }
    
        public long getLocationId() {
        return locationId;
    }
    
    public void setLocationId(long locationId) {
        this.locationId = locationId;
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
    
    public long getPeriodeId() {
        return periodeId;
    }
    
    public void setPeriodeId(long periodeId) {
        this.periodeId = periodeId;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
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
    
    public int getType() {
        return this.type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public long getMerkId() {
        return merkId;
    }
    
    public void setMerkId(long merkId) {
        this.merkId = merkId;
    }
    
    public int getStockValueBy() {
        return stockValueBy;
    }
    
    public void setStockValueBy(int stockValueBy) {
        this.stockValueBy = stockValueBy;
    }
    
    public int getInfoShowed() {
        return infoShowed;
    }
    
    public void setInfoShowed(int infoShowed) {
        this.infoShowed = infoShowed;
    }
    
    public long getUserId() {
        return userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public boolean getGenerateReport() {
        return generateReport;
    }
    
    public void setGeneratereport(boolean generateReport) {
        this.generateReport = generateReport;
    }
    
    public String getMaterialName() {
        return materialName;
    }
    
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /**
     * @return the sortBy
     */
    public int getSortBy() {
        return sortBy;
    }

    /**
     * @param sortBy the sortBy to set
     */
    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }
}
