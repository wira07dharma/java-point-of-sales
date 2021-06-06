package com.dimata.posbo.entity.warehouse;

/* package java */
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.PstCategory;
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;
import com.dimata.util.Formater;
import java.util.Vector;

public class MatStockOpname extends Entity implements I_LogHistory {

    /**
     * @return the masterGroupId
     */
    public long getMasterGroupId() {
        return masterGroupId;
    }

    /**
     * @param masterGroupId the masterGroupId to set
     */
    public void setMasterGroupId(long masterGroupId) {
        this.masterGroupId = masterGroupId;
    }

 

    private long locationId = 0;
    private Date stockOpnameDate = new Date();
    private String stockOpnameTime = "";
    private String stockOpnameNumber = "";
    private int stockOpnameStatus = 0;
    private String remark = "";
    private long supplierId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private Vector listOpnameItem = new Vector();
    private long etalaseId = 0;
    private int opnameItemType = 0;
    private int codeCounter = 0;
    private long masterGroupId = 0;

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

    public int getSizeListOpnameItem() {
        return listOpnameItem.size();
    }

    public void addListOpnameItem(MatStockOpnameItem matStockOpnameItem) {
        listOpnameItem.add(matStockOpnameItem);
    }

    public MatStockOpnameItem getListOpnameItem(int idx) {
        if (idx < 0 || idx >= getSizeListOpnameItem()) {
            return null;
        }
        return getListOpnameItem(idx);
    }

    public Vector getListOpnameItem() {
        return listOpnameItem;
    }

    public void deleteListOpnameItem(MatStockOpnameItem matStockOpnameItem) {
        listOpnameItem.remove(matStockOpnameItem);
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public Date getStockOpnameDate() {
        return stockOpnameDate;
    }

    public void setStockOpnameDate(Date stockOpnameDate) {
        this.stockOpnameDate = stockOpnameDate;
    }

    public String getStockOpnameTime() {
        return stockOpnameTime;
    }

    public void setStockOpnameTime(String stockOpnameTime) {
        this.stockOpnameTime = stockOpnameTime;
    }

    public String getStockOpnameNumber() {
        return stockOpnameNumber;
    }

    public void setStockOpnameNumber(String stockOpnameNumber) {
        this.stockOpnameNumber = stockOpnameNumber;
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

    public int getStockOpnameStatus() {
        return stockOpnameStatus;
    }

    public void setStockOpnameStatus(int stockOpnameStatus) {
        this.stockOpnameStatus = stockOpnameStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
     /**
   * @return the codeCounter
   */
  public int getCodeCounter() {
    return codeCounter;
  }

  /**
   * @param codeCounter the codeCounter to set
   */
  public void setCodeCounter(int codeCounter) {
    this.codeCounter = codeCounter;
  }

        //by dyas 20131127
    //tambah methods getLogDetail
    public String getLogDetail(Entity prevDoc) {
        MatStockOpname prevMatStockOpname = (MatStockOpname) prevDoc;
        Location location = new Location();
        ContactList contactList = new ContactList();
        Category category = new Category();

        try {
            if (this != null && getSupplierId() != 0 && (prevMatStockOpname == null || prevMatStockOpname.getOID() == 0 || prevMatStockOpname.getSupplierId() != this.getSupplierId())) {
                contactList = PstContactList.fetchExc(getSupplierId());
            }
            //=====================================================================================
            if (this != null && getLocationId() != 0) {
                location = PstLocation.fetchExc(getLocationId());
            }
            if (this != null && getCategoryId() != 0) {
                category = PstCategory.fetchExc(getCategoryId());
            }
        } catch (Exception exc) {

        }

        return (prevMatStockOpname == null || prevMatStockOpname.getOID() == 0 || prevMatStockOpname.getLocationId() == 0 || prevMatStockOpname.getLocationId() != this.getLocationId()
                ? ("Location : " + location.getName() + " ;") : "")
                + (prevMatStockOpname == null || prevMatStockOpname.getOID() == 0 || !Formater.formatDate(prevMatStockOpname.getStockOpnameDate(), "yyyy-MM-dd").equals(Formater.formatDate(this.getStockOpnameDate(), "yyyy-MM-dd"))
                        ? (" Date Time : " + Formater.formatDate(this.getStockOpnameDate(), "yyyy-MM-dd") + " ;") : "")
                + (contactList != null
                        ? (" Supplier : " + contactList.getCompName() + " ;"
                        + " Contact : " + contactList.getPersonName() + " ;"
                        + " Address : " + contactList.getBussAddress() + " ;"
                        + " Telephone. : " + contactList.getTelpNr() + " ;") : "")
                + (prevMatStockOpname == null || prevMatStockOpname.getOID() == 0 || prevMatStockOpname.getCategoryId() == 0 || prevMatStockOpname.getCategoryId() != this.getCategoryId()
                        ? ("Category : " + category != null ? category.getName() : "" + " ;") : "")
                + ((prevMatStockOpname == null || prevMatStockOpname.getOID() == 0 || prevMatStockOpname.getStockOpnameStatus() != this.getStockOpnameStatus())
                        ? (" Status : " + I_DocStatus.fieldDocumentStatus[this.getStockOpnameStatus()]) : "")
                + ((prevMatStockOpname == null || prevMatStockOpname.getOID() == 0 || prevMatStockOpname.getRemark() == null || prevMatStockOpname.getRemark().compareToIgnoreCase(this.getRemark()) != 0)
                        ? (" Remark : " + this.getRemark() + " ;") : "");
    }

}
