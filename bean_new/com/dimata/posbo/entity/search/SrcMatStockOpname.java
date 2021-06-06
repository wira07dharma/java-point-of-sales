package com.dimata.posbo.entity.search;

/* package java */
import java.util.Date;
import java.util.Vector;

public class SrcMatStockOpname {

    private long locationId;
    private int statusDate;
    private Date fromDate = new Date();
    private Date toDate = new Date();
    private int sortBy;

    private int status;
    private long supplierId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private String opnameNumber = "";
    private Vector docStatus = new Vector(1,1);    
    
    private int groupBy;
    
    //added by dewok 2018 for jewel    
    private long etalaseId = 0;
    private int opnameItemType = 0;
    
    public String getOpnameNumber() {
	return opnameNumber;
    }

    public void setOpnameNumber(String opnameNumber) {
	this.opnameNumber = opnameNumber;
    }

    public long getLocationId() {
	return locationId;
    }

    public void setLocationId(long locationId) {
	this.locationId = locationId;
    }

    public int getStatusDate() {
	return statusDate;
    }

    public void setStatusDate(int statusDate) {
	this.statusDate = statusDate;
    }

    public Date getFromDate() {
	return fromDate;
    }

    public void setFromDate(Date fromDate) {
	this.fromDate = fromDate;
    }

    public Date getToDate() {
	return toDate;
    }

    public void setToDate(Date toDate) {
	this.toDate = toDate;
    }

    public int getSortBy() {
	return sortBy;
    }

    public void setSortBy(int sortBy) {
	this.sortBy = sortBy;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
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

    public Vector getDocStatus() {
        return docStatus;
    }
    
    public void setDocStatus(Vector docStatus) {
        this.docStatus = docStatus;
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

    public long getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(long etalaseId) {
        this.etalaseId = etalaseId;
    }

    public int getOpnameItemType() {
        return opnameItemType;
    }

    public void setOpnameItemType(int opnameItemType) {
        this.opnameItemType = opnameItemType;
    }
    
}
