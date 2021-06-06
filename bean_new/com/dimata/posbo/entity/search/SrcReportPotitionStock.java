/*
 * SrcReportPotitionStock.java
 *
 * Created on February 18, 2008, 9:55 AM
 */

package com.dimata.posbo.entity.search;

import java.util.Date;
import java.util.Vector;

/**
 *
 * @author gwawan
 */
public class SrcReportPotitionStock {

    /**
     * @return the showQtyZero
     */
    public int getShowQtyZero() {
        return showQtyZero;
    }

    /**
     * @param showQtyZero the showQtyZero to set
     */
    public void setShowQtyZero(int showQtyZero) {
        this.showQtyZero = showQtyZero;
    }
    
    /** Creates a new instance of SrcReportPotitionStock */
    public SrcReportPotitionStock() {
    }
    
    // konstanta untuk menentukkan apakah nilai stok meupakan hasil kali dengan HPP dari master atau HPP dari transaksi
    public static final int STOCK_VALUE_BY_COGS_MASTER = 0;
    public static final int STOCK_VALUE_BY_COGS_TRANSACTION = 1;
    
    public static String stringStockValueBy[][] = {
        {"HPP Master","HPP Transaksi"},
        {"COGS Master","COGS Transaction"}
    };
    
    public static final int SHOW_BOTH = 0;
    public static final int SHOW_QTY_ONLY = 1;
    public static final int SHOW_VALUE_ONLY = 2;
    
    public static String stringInfoShowed[][] = {
        {"Semua","Jumlah Stok","Nilai Stok"},
        {"Both","Quantity Stock","Stock Value"}
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
    private int statusDate;
    private long merkId = 0;
    private int stockValueBy = 0;
    private int infoShowed = 0; //variable ini digunakan untuk menentukkan tipe informasi yang akan ditampilkan
    private long userId = 0; //variabel ini untuk membedakan antara laporan berdasarkan user yang men-generate-nya
    private boolean generateReport = false; //variabel ini untuk menentukkan apakah laporan perlu di-generate ulang
    private long stockValueSale=0;
    
    
    //add opie-eyek stok berdasarkan nilai jual
    private long  standartId=0;
    private int groupBy=0;
    private long priceTypeId=0;
    private Vector vPriceTypeId= new Vector();
    private int viedDeadStok=0;
    private int incWH = 0;
    private int showQtyZero = 0;
    
    //tambahin aja
    private int typeQuery=0;
    
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
     * @return the standartId
     */
    public long getStandartId() {
        return standartId;
    }

    /**
     * @param standartId the standartId to set
     */
    public void setStandartId(long standartId) {
        this.standartId = standartId;
    }

    /**
     * @return the stockValueSale
     */
    public long getStockValueSale() {
        return stockValueSale;
    }

    /**
     * @param stockValueSale the stockValueSale to set
     */
    public void setStockValueSale(long stockValueSale) {
        this.stockValueSale = stockValueSale;
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
     * @return the vPriceTypeId
     */
    public Vector getvPriceTypeId() {
        return vPriceTypeId;
    }

    /**
     * @param vPriceTypeId the vPriceTypeId to set
     */
    public void setvPriceTypeId(Vector vPriceTypeId) {
        this.vPriceTypeId = vPriceTypeId;
    }

    /**
     * @return the typeQuery
     */
    public int getTypeQuery() {
        return typeQuery;
    }

    /**
     * @param typeQuery the typeQuery to set
     */
    public void setTypeQuery(int typeQuery) {
        this.typeQuery = typeQuery;
    }

    /**
     * @return the viedDeadStok
     */
    public int getViedDeadStok() {
        return viedDeadStok;
    }

    /**
     * @param viedDeadStok the viedDeadStok to set
     */
    public void setViedDeadStok(int viedDeadStok) {
        this.viedDeadStok = viedDeadStok;
    }

	/**
	 * @return the incWH
	 */
	public int getIncWH() {
		return incWH;
	}

	/**
	 * @param incWH the incWH to set
	 */
	public void setIncWH(int incWH) {
		this.incWH = incWH;
	}
}


