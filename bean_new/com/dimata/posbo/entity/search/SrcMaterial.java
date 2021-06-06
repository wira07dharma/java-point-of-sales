package com.dimata.posbo.entity.search;

/* package java */
import java.util.*;

public class SrcMaterial {

    private String matcode = "";
    private String matname = "";
    private long supplierId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private int sortby;
    private String description = "";
    private long locationId;
    private int typeItem = -1;
    private long merkId;

    // new for colonias
    private String codeShip = "";
    private String codeCounter = "";
    private int jenisCode = 0;
    private int showImage = 0;
    private long oidCodeRange = 0;
    private String barCode = "";

    //new for update harga catalog
    private int showUpdateCatalog = 0;
    private Date dateFrom = new Date();
    private Date dateTo = new Date();

    //new for discount Qty
    private int  showDiscountQty = 0;

    //new for opsi stok 0
    private int showStokNol = 0;

    //new for opsi currencyTypeId
    private Vector currencyTypeId = new Vector(1,1);

    //new for opsi memberType
    private Vector memberTypeId = new Vector(1,1);
    
    private Vector sellLocation = new Vector(1,1);

    //new for opsi printHpp
    private int showHpp = 0;

    //new for opsi currency
    private int showCurrency = 0;

    //new for search composit
    private int groupItem = 0;
    
    private Vector priceTypeId = new Vector(1,1);

    //add opie-eyek 20130805
    private long selectPriceTypeId;
    private long selectCurrencyTypeId;
    private int useSerialNumber;
    //add opie-eyek 20140415
    private int statusMaterial;
    private Vector listTypeHrga = new Vector(1,1);
    
    private Vector vBarcode = new Vector(1,1);
    private Vector vNameMaterial = new Vector(1,1);
    private Vector qtyPrintBarcode = new Vector(1,1);
    private Vector priceBarcode = new Vector(1,1);
    private long gondolaId=0;
    private int viewHppvsPrice=0;
            
    private int designMat=0;
    
    public long getOidCodeRange() {
        return oidCodeRange;
    }

    public void setOidCodeRange(long oidCodeRange) {
        this.oidCodeRange = oidCodeRange;
    }

    public int getShowImage() {
        return showImage;
    }

    public void setShowImage(int showImage) {
        this.showImage = showImage;
    }

    public int getJenisCode() {
        return jenisCode;
    }

    public void setJenisCode(int jenisCode) {
        this.jenisCode = jenisCode;
    }

    public String getCodeShip() {
        return codeShip;
    }

    public void setCodeShip(String codeShip) {
        this.codeShip = codeShip;
    }

    public String getCodeCounter() {
        return codeCounter;
    }

    public void setCodeCounter(String codeCounter) {
        this.codeCounter = codeCounter;
    }

    public String getMatcode() {
        return matcode;
    }

    public void setMatcode(String matcode) {
        if (matcode == null) {
            matcode = "";
        }
        this.matcode = matcode;
    }

    public String getMatname() {
        return matname;
    }

    public void setMatname(String matname) {
        if (matname == null) {
            matname = "";
        }
        this.matname = matname;
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

    public int getSortby() {
        return sortby;
    }

    public void setSortby(int sortby) {
        this.sortby = sortby;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        this.description = description;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /** Getter for property typeItem.
     * @return Value of property typeItem.
     *
     */
    public int getTypeItem() {
        return this.typeItem;
    }

    /** Setter for property typeItem.
     * @param typeItem New value of property typeItem.
     *
     */
    public void setTypeItem(int typeItem) {
        this.typeItem = typeItem;
    }

    /** Getter for property merkId.
     * @return Value of property merkId.
     *
     */
    public long getMerkId() {
        return this.merkId;
    }

    /** Setter for property merkId.
     * @param merkId New value of property merkId.
     *
     */
    public void setMerkId(long merkId) {
        this.merkId = merkId;
    }

    /**
     * @return the barCode
     */
    public String getBarCode() {
        return barCode;
    }

    /**
     * @param barCode the barCode to set
     */
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    /**
     * @return the showUpdateCatalog
     */
    public int getShowUpdateCatalog() {
        return showUpdateCatalog;
    }

    /**
     * @param showUpdateCatalog the showUpdateCatalog to set
     */
    public void setShowUpdateCatalog(int showUpdateCatalog) {
        this.showUpdateCatalog = showUpdateCatalog;
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the showDiscountQty
     */
    public int getShowDiscountQty() {
        return showDiscountQty;
    }

    /**
     * @param showDiscountQty the showDiscountQty to set
     */
    public void setShowDiscountQty(int showDiscountQty) {
        this.showDiscountQty = showDiscountQty;
    }

    /**
     * @return the showStokNol
     */
    public int getShowStokNol() {
        return showStokNol;
    }

    /**
     * @param showStokNol the showStokNol to set
     */
    public void setShowStokNol(int showStokNol) {
        this.showStokNol = showStokNol;
    }

    /**
     * @return the currencyTypeId
     */
    public Vector getCurrencyTypeId() {
        return currencyTypeId;
    }

    /**
     * @param currencyTypeId the currencyTypeId to set
     */
    public void setCurrencyTypeId(Vector currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    /**
     * @return the memberTypeId
     */
    public Vector getMemberTypeId() {
        return memberTypeId;
    }

    /**
     * @param memberTypeId the memberTypeId to set
     */
    public void setMemberTypeId(Vector memberTypeId) {
        this.memberTypeId = memberTypeId;
    }

    /**
     * @return the showHpp
     */
    public int getShowHpp() {
        return showHpp;
    }

    /**
     * @param showHpp the showHpp to set
     */
    public void setShowHpp(int showHpp) {
        this.showHpp = showHpp;
    }

    /**
     * @return the showCurrency
     */
    public int getShowCurrency() {
        return showCurrency;
    }

    /**
     * @param showCurrency the showCurrency to set
     */
    public void setShowCurrency(int showCurrency) {
        this.showCurrency = showCurrency;
    }

    /**
     * @return the groupItem
     */
    public int getGroupItem() {
        return groupItem;
    }

    /**
     * @param groupItem the groupItem to set
     */
    public void setGroupItem(int groupItem) {
        this.groupItem = groupItem;
    }

    /**
     * @return the priceType
     */
    public Vector getPriceTypeId() {
        return priceTypeId;
    }

    /**
     * @param priceType the priceType to set
     */
    public void setPriceTypeId(Vector priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    /**
     * @return the selectPriceTypeId
     */
    public long getSelectPriceTypeId() {
        return selectPriceTypeId;
    }

    /**
     * @param selectPriceTypeId the selectPriceTypeId to set
     */
    public void setSelectPriceTypeId(long selectPriceTypeId) {
        this.selectPriceTypeId = selectPriceTypeId;
    }

    /**
     * @return the selectCurrencyTypeId
     */
    public long getSelectCurrencyTypeId() {
        return selectCurrencyTypeId;
    }

    /**
     * @param selectCurrencyTypeId the selectCurrencyTypeId to set
     */
    public void setSelectCurrencyTypeId(long selectCurrencyTypeId) {
        this.selectCurrencyTypeId = selectCurrencyTypeId;
    }

    /**
     * @return the useSerialNumber
     */
    public int getUseSerialNumber() {
        return useSerialNumber;
    }

    /**
     * @param useSerialNumber the useSerialNumber to set
     */
    public void setUseSerialNumber(int useSerialNumber) {
        this.useSerialNumber = useSerialNumber;
    }

    /**
     * @return the statusMaterial
     */
    public int getStatusMaterial() {
        return statusMaterial;
    }

    /**
     * @param statusMaterial the statusMaterial to set
     */
    public void setStatusMaterial(int statusMaterial) {
        this.statusMaterial = statusMaterial;
    }

    /**
     * @return the listTypeHrga
     */
    public Vector getListTypeHrga() {
        return listTypeHrga;
    }

    /**
     * @param listTypeHrga the listTypeHrga to set
     */
    public void setListTypeHrga(Vector listTypeHrga) {
        this.listTypeHrga = listTypeHrga;
}

    /**
     * @return the sellLocation
     */
    public Vector getSellLocation() {
        return sellLocation;
    }

    /**
     * @param sellLocation the sellLocation to set
     */
    public void setSellLocation(Vector sellLocation) {
        this.sellLocation = sellLocation;
    }

    /**
     * @return the designMat
     */
    public int getDesignMat() {
        return designMat;
    }

    /**
     * @param designMat the designMat to set
     */
    public void setDesignMat(int designMat) {
        this.designMat = designMat;
    }

    /**
     * @return the vBarcode
     */
    public Vector getvBarcode() {
        return vBarcode;
    }

    /**
     * @param vBarcode the vBarcode to set
     */
    public void setvBarcode(Vector vBarcode) {
        this.vBarcode = vBarcode;
    }

    /**
     * @return the vNameMaterial
     */
    public Vector getvNameMaterial() {
        return vNameMaterial;
    }

    /**
     * @param vNameMaterial the vNameMaterial to set
     */
    public void setvNameMaterial(Vector vNameMaterial) {
        this.vNameMaterial = vNameMaterial;
    }

    /**
     * @return the qtyPrintBarcode
     */
    public Vector getQtyPrintBarcode() {
        return qtyPrintBarcode;
    }

    /**
     * @param qtyPrintBarcode the qtyPrintBarcode to set
     */
    public void setQtyPrintBarcode(Vector qtyPrintBarcode) {
        this.qtyPrintBarcode = qtyPrintBarcode;
    }

    /**
     * @return the priceBarcode
     */
    public Vector getPriceBarcode() {
        return priceBarcode;
    }

    /**
     * @param priceBarcode the priceBarcode to set
     */
    public void setPriceBarcode(Vector priceBarcode) {
        this.priceBarcode = priceBarcode;
    }

    /**
     * @return the gondolaId
     */
    public long getGondolaId() {
        return gondolaId;
    }

    /**
     * @param gondolaId the gondolaId to set
     */
    public void setGondolaId(long gondolaId) {
        this.gondolaId = gondolaId;
    }

    /**
     * @return the viewHppvsPrice
     */
    public int getViewHppvsPrice() {
        return viewHppvsPrice;
    }

    /**
     * @param viewHppvsPrice the viewHppvsPrice to set
     */
    public void setViewHppvsPrice(int viewHppvsPrice) {
        this.viewHppvsPrice = viewHppvsPrice;
    }


}
