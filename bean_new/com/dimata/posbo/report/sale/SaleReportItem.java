/*
 * SaleReportItem.java
 *
 * Created on February 19, 2005, 10:26 AM
 */
package com.dimata.posbo.report.sale;

import java.util.*;
import java.io.*;

/**
 *
 * @author wpradnyana
 */
public class SaleReportItem implements Serializable {

    /**
     * Holds value of property dateFrom.
     */
    private Date dateFrom;

    /**
     * Holds value of property dateTo.
     */
    private Date dateTo;

    /**
     * Holds value of property shiftId.
     */
    private long shiftId;

    /**
     * Holds value of property shiftName.
     */
    private String shiftName;

    /**
     * Holds value of property categoryId.
     */
    private long categoryId;

    /**
     * Holds value of property categoryName.
     */
    private String categoryName;

    /**
     * Holds value of property subCategoryId.
     */
    private long subCategoryId;

    /**
     * Holds value of property subCategoryName.
     */
    private String subCategoryName;

    /**
     * Holds value of property itemId.
     */
    private long itemId;

    /**
     * Holds value of property itemName.
     */
    private String itemName;

    /**
     * Holds value of property markId.
     */
    private long markId;

    /**
     * Holds value of property markName.
     */
    private String markName;

    /**
     * Holds value of property locationId.
     */
    private long locationId;

    /**
     * Holds value of property locationName.
     */
    private String locationName;

    /**
     * Holds value of property supplierId.
     */
    private long supplierId;

    /**
     * Holds value of property supplierName.
     */
    private String supplierName;

    /**
     * Holds value of property salesPersonId.
     */
    private long salesPersonId;

    /**
     * Holds value of property salesPersonName.
     */
    private String salesPersonName;

    /**
     * Holds value of property transType.
     */
    private int transType;

    /**
     * Holds value of property itemQty.
     */
    private double itemQty;

    /**
     * Holds value of property totalQty.
     */
    private double totalQty;

    /**
     * Holds value of property totalSold.
     */
    private double totalSold;

    /**
     * Holds value of property totalCost.
     */
    private double totalCost;

    /**
     * Holds value of property totalMargin.
     */
    private double totalMargin;

    private double cost = 0.0;

    //add opie-eyek 20160803
    private int totTransaksi = 0;
    private String itemBarcode = "";

    private double price = 0.0;

    // new
    private double discount = 0.0;

    /**
     * Holds value of property totalMarginPct.
     */
    private double totalMarginPct;

    private String itemCode;

    private double totalQtyByStock = 0;
    private double totalSoldByStock = 0;

    //added by dewok 20181207 for greenbowl sales report
    private String billNumber;
    private Date billDate;
    private long masterTypeId;
    private String masterTypeName;

    public long getMasterTypeId() {
        return masterTypeId;
    }

    public void setMasterTypeId(long masterTypeId) {
        this.masterTypeId = masterTypeId;
    }

    public String getMasterTypeName() {
        return masterTypeName;
    }

    public void setMasterTypeName(String masterTypeName) {
        this.masterTypeName = masterTypeName;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * Creates a new instance of SaleReportItem
     */
    public SaleReportItem() {
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
     * Getter for property shiftName.
     *
     * @return Value of property shiftName.
     */
    public String getShiftName() {
        return this.shiftName;
    }

    /**
     * Setter for property shiftName.
     *
     * @param shiftName New value of property shiftName.
     */
    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    /**
     * Getter for property kategoriId.
     *
     * @return Value of property kategoriId.
     */
    public long getCategoryId() {
        return this.categoryId;
    }

    /**
     * Setter for property kategoriId.
     *
     * @param kategoriId New value of property kategoriId.
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Getter for property categoryName.
     *
     * @return Value of property categoryName.
     */
    public String getCategoryName() {
        return this.categoryName;
    }

    /**
     * Setter for property categoryName.
     *
     * @param categoryName New value of property categoryName.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Getter for property subKategoryId.
     *
     * @return Value of property subKategoryId.
     */
    public long getSubCategoryId() {
        return this.subCategoryId;
    }

    /**
     * Setter for property subKategoryId.
     *
     * @param subKategoryId New value of property subKategoryId.
     */
    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    /**
     * Getter for property subCategoryName.
     *
     * @return Value of property subCategoryName.
     */
    public String getSubCategoryName() {
        return this.subCategoryName;
    }

    /**
     * Setter for property subCategoryName.
     *
     * @param subCategoryName New value of property subCategoryName.
     */
    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
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
     * Getter for property merkId.
     *
     * @return Value of property merkId.
     */
    public long getMarkId() {
        return this.markId;
    }

    /**
     * Setter for property merkId.
     *
     * @param merkId New value of property merkId.
     */
    public void setMarkId(long markId) {
        this.markId = markId;
    }

    /**
     * Getter for property merkName.
     *
     * @return Value of property merkName.
     */
    public String getMarkName() {
        return this.markName;
    }

    /**
     * Setter for property merkName.
     *
     * @param merkName New value of property merkName.
     */
    public void setMarkName(String markName) {
        this.markName = markName;
    }

    /**
     * Getter for property lokasiId.
     *
     * @return Value of property lokasiId.
     */
    public long getLocationId() {
        return this.locationId;
    }

    /**
     * Setter for property lokasiId.
     *
     * @param lokasiId New value of property lokasiId.
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * Getter for property lokasiName.
     *
     * @return Value of property lokasiName.
     */
    public String getLocationName() {
        return this.locationName;
    }

    /**
     * Setter for property lokasiName.
     *
     * @param lokasiName New value of property lokasiName.
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
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
     * Getter for property supplierName.
     *
     * @return Value of property supplierName.
     */
    public String getSupplierName() {
        return this.supplierName;
    }

    /**
     * Setter for property supplierName.
     *
     * @param supplierName New value of property supplierName.
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
     * Getter for property salesPersonName.
     *
     * @return Value of property salesPersonName.
     */
    public String getSalesPersonName() {
        return this.salesPersonName;
    }

    /**
     * Setter for property salesPersonName.
     *
     * @param salesPersonName New value of property salesPersonName.
     */
    public void setSalesPersonName(String salesPersonName) {
        this.salesPersonName = salesPersonName;
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
     * Getter for property itemQty.
     *
     * @return Value of property itemQty.
     */
    public double getItemQty() {
        return this.itemQty;
    }

    /**
     * Setter for property itemQty.
     *
     * @param itemQty New value of property itemQty.
     */
    public void setItemQty(double itemQty) {
        this.itemQty = itemQty;
    }

    /**
     * Getter for property totalQty.
     *
     * @return Value of property totalQty.
     */
    public double getTotalQty() {
        return this.totalQty;
    }

    /**
     * Setter for property totalQty.
     *
     * @param totalQty New value of property totalQty.
     */
    public void setTotalQty(double totalQty) {
        this.totalQty = totalQty;
    }

    /**
     * Getter for property totalSold.
     *
     * @return Value of property totalSold.
     */
    public double getTotalSold() {
        return this.totalSold;
    }

    /**
     * Setter for property totalSold.
     *
     * @param totalSold New value of property totalSold.
     */
    public void setTotalSold(double totalSold) {
        this.totalSold = totalSold;
    }

    public String showValues() {
        StringBuffer buff = new StringBuffer();
        buff.append("\n getCategoryId " + this.getCategoryId());
        buff.append("\n getCategoryName" + this.getCategoryName());
        buff.append("\n getDateFrom" + this.getDateFrom().toString());
        buff.append("\n getDateTo" + this.getDateTo().toString());
        buff.append("\n getItemId" + this.getItemId());
        buff.append("\n getItemName" + this.getItemName());
        buff.append("\n getItemQty" + this.getItemQty());
        buff.append("\n getLocationId" + this.getLocationId());
        buff.append("\n getLocationName" + this.getLocationName());
        buff.append("\n getMarkId" + this.getMarkId());
        buff.append("\n getMarkName" + this.getMarkName());
        buff.append("\n getSalesPersonId" + this.getSalesPersonId());
        buff.append("\n getSalesPersonName" + this.getSalesPersonName());
        buff.append("\n getShiftId" + this.getShiftId());
        buff.append("\n getShiftName" + this.getShiftName());
        buff.append("\n getSubCategoryId" + this.getSubCategoryId());
        buff.append("\n getSubCategoryName" + this.getSubCategoryName());
        buff.append("\n getSupplierId" + this.getSupplierId());
        buff.append("\n getSupplierName" + this.getSupplierName());
        buff.append("\n getTotalQty" + this.getTotalQty());
        buff.append("\n getTotalSold" + this.getTotalSold());
        buff.append("\n getTransType" + this.getTransType());
        buff.append("\n getTotalQtyByStock" + this.getTotalQtyByStock());
        buff.append("\n getTotalSoldByStock" + this.getTotalSoldByStock());
        String values = new String(buff);
        return values;
    }

    /**
     * Getter for property totalCost.
     *
     * @return Value of property totalCost.
     */
    public double getTotalCost() {
        return this.totalCost;
    }

    /**
     * Setter for property totalCost.
     *
     * @param totalCost New value of property totalCost.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Getter for property margin.
     *
     * @return Value of property margin.
     */
    public double getTotalMargin() {
        return this.totalMargin;
    }

    /**
     * Setter for property margin.
     *
     * @param margin New value of property margin.
     */
    public void setTotalMargin(double totalMargin) {
        this.totalMargin = totalMargin;
    }

    /**
     * Getter for property totalMarginPct.
     *
     * @return Value of property totalMarginPct.
     */
    public double getTotalMarginPct() {
        return this.totalMarginPct;
    }

    /**
     * Setter for property totalMarginPct.
     *
     * @param totalMarginPct New value of property totalMarginPct.
     */
    public void setTotalMarginPct(double totalMarginPct) {
        this.totalMarginPct = totalMarginPct;
    }

    /**
     * Getter for property totalQtyByStock.
     *
     * @return Value of property totalQtyByStock.
     */
    public double getTotalQtyByStock() {
        return totalQtyByStock;
    }

    /**
     * Setter for property totalQtyByStock.
     *
     * @param qtyStock New value of property totalQtyByStock.
     */
    public void setTotalQtyByStock(double totalQtyByStock) {
        this.totalQtyByStock = totalQtyByStock;
    }

    /**
     * Getter for property totalSoldByStock.
     *
     * @return Value of property totalSoldByStock.
     */
    public double getTotalSoldByStock() {
        return totalSoldByStock;
    }

    /**
     * Setter for property totalSoldByStock.
     *
     * @param totalPriceStock New value of property totalSoldByStock.
     */
    public void setTotalSoldByStock(double totalSoldByStock) {
        this.totalSoldByStock = totalSoldByStock;
    }

    /**
     * @return the totTransaksi
     */
    public int getTotTransaksi() {
        return totTransaksi;
    }

    /**
     * @param totTransaksi the totTransaksi to set
     */
    public void setTotTransaksi(int totTransaksi) {
        this.totTransaksi = totTransaksi;
    }

    /**
     * @return the itemBarcode
     */
    public String getItemBarcode() {
        return itemBarcode;
    }

    /**
     * @param itemBarcode the itemBarcode to set
     */
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

}
