package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.util.Formater;

public class Material extends Entity implements I_LogHistory {

    /**
     * @return the viewInChart
     */
    public int getViewInChart() {
        return viewInChart;
    }

    /**
     * @param viewInChart the viewInChart to set
     */
    public void setViewInChart(int viewInChart) {
        this.viewInChart = viewInChart;
    }


    /**
     * ini di gunakan untuk membedakan nama di gabung dengan kategori dan merk
     * atau kebalikan, input secara manual atau tidak
     */
    public static final int WITH_USE_SWITCH_MERGE_AUTOMATIC = 0;
    public static final int WITH_USE_SWITCH_MERGE_MANUAL = 1;
    private static int materialSwitchType = WITH_USE_SWITCH_MERGE_MANUAL;

    public static final String orderBy[] = {"SKU", "Category", "Name"};
    public static final String orderByValue[] = {"0", "1", "2"};
    public static int getMaterialSwitchType() {
        return materialSwitchType;
    }

    public static void setMaterialSwitchType(int tp) {
        materialSwitchType = tp;
    }

    public static String getSeparate() {
        return "/";
    }

    public static String getSeparateLanguage() {
        return ";";
    }

    public static String getReplaceSeparate() {
        return " ";
    }
    //end ----------------
    /**
     * ini di gunakan unruk membedakan pas get barang nanti proses
     * insert/update/ view saja
     */
    public static final int IS_PROCESS_VIEW = 0;
    public static final int IS_PROCESS_INSERT_UPDATE = 1;
    public static final int IS_PROCESS_SHORT_VIEW = 2;
    private int isProses = IS_PROCESS_SHORT_VIEW;

    public static final int MATERIAL_TYPE_GENERAL = 0;
    public static final int MATERIAL_TYPE_EMAS = 1;
    public static final int MATERIAL_TYPE_BERLIAN = 2;
    public static final int MATERIAL_TYPE_EMAS_LANTAKAN = 3;    

    public static final String MATERIAL_TYPE_TITLE[] = {"General", "Emas", "Berlian", "Emas Lantakan"};

    public static final int MATERIAL_TYPE_RECEIVING_GENERAL = 0;
    public static final int MATERIAL_TYPE_RECEIVING_SPECIFIC = 1;
    public static final String MATERIAL_TYPE_RECEIVING_TITLE[] = {"General", "Specific"};

    public static final int MATERIAL_TYPE_RECEIVING_INDEX[] = {
        MATERIAL_TYPE_RECEIVING_GENERAL,
        MATERIAL_TYPE_RECEIVING_SPECIFIC,
        MATERIAL_TYPE_RECEIVING_SPECIFIC,
        MATERIAL_TYPE_RECEIVING_SPECIFIC
    };
	
	public int getProses() {
        return isProses;
    }

    public void setProses(int proses) {
        isProses = proses;
    }
    //end ----------------
    private String sku = "";
    private String barCode = "";
    private String name = "";
    private long merkId = 0;
    private long categoryId = 0;
    private long subCategoryId = 0;
    private long defaultStockUnitId = 0;
    private double defaultPrice = 0.00;
    private long defaultPriceCurrencyId = 0;
    private double defaultCost = 0.00;
    private long defaultCostCurrencyId = 0;
    private int defaultSupplierType = 0;
    private long supplierId = 0;
    private double priceType01 = 0.00;
    private double priceType02 = 0.00;
    private double priceType03 = 0.00;
    private int materialType = 0;
    private double lastDiscount = 0.00;
    private double lastVat = 0.00;
    private double currBuyPrice = 0.00;
    private long buyUnitId = 0;
    private Date expiredDate = new Date();
    private double profit = 0.0;
    private double currSellPriceRecomentation = 0.0;
    private long colorId = 0;
    /**
     * Holds value of property averagePrice.
     */
    private double averagePrice = 0;
    /**
     * Holds value of property minimumPoint.
     */
    private int minimumPoint = 0;
    /**
     * Holds value of property requiredSerialNumber.
     */
    private int requiredSerialNumber = 0;
    /**
     * Holds value of property lastUpdate.
     */
    private Date lastUpdate;
    /**
     * Holds value of property processStatus.
     */
    private int processStatus = 0;
    // new 
    // this use for diff catalog is consigment or no
    private int matTypeConsig = 0;

    // for request SMU
    private long gondolaCode = 0;
    private String gondolaName = "";

    // for updated catalog
    private java.util.Date updateDate;

    // for update last cost cargo
    private double lastCostCargo;

    //for update edit material
    //add opie 12-06-2012
    private int editMaterial = 0;

    //add opie-eyek 20130903
    private int pointSales = 0;

    private String materialDescription = "";
    private String Barcode;
    private String MaterialId;

    private long useSellLocation = 0;

    private long kepemilikanId = 0;
    private long posColor = 0;
    private long posKadar = 0;
    private int materialJenisType = 0;
    private String materialImage = "";
	private int salesRule = 0;
	private int returnRule = 0;
	
	private int chainType = 0;
        private int viewInChart = 0;

    public String getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(String materialImage) {
        this.materialImage = materialImage;
    }

    public int getMaterialJenisType() {
        return materialJenisType;
    }

    public void setMaterialJenisType(int materialJenisType) {
        this.materialJenisType = materialJenisType;
    }

    public int getMatTypeConsig() {
        return matTypeConsig;
    }

    public void setMatTypeConsig(int matTypeConsig) {
        this.matTypeConsig = matTypeConsig;
    }

    public double getCurrSellPriceRecomentation() {
        return currSellPriceRecomentation;
    }

    public void setCurrSellPriceRecomentation(double currSellPriceRecomentation) {
        this.currSellPriceRecomentation = currSellPriceRecomentation;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public long getBuyUnitId() {
        return buyUnitId;
    }

    public void setBuyUnitId(long buyUnitId) {
        this.buyUnitId = buyUnitId;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public double getLastDiscount() {
        return lastDiscount;
    }

    public void setLastDiscount(double lastDiscount) {
        this.lastDiscount = lastDiscount;
    }

    public double getLastVat() {
        return lastVat;
    }

    public void setLastVat(double lastVat) {
        this.lastVat = lastVat;
    }

    public double getCurrBuyPrice() {
        return currBuyPrice;
    }

    public void setCurrBuyPrice(double currBuyPrice) {
        this.currBuyPrice = currBuyPrice;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        if (sku == null) {
            sku = "";
        }
        this.sku = sku;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getName() {
        switch (getMaterialSwitchType()) {
            case WITH_USE_SWITCH_MERGE_AUTOMATIC:
                if (getProses() == IS_PROCESS_VIEW) {
                    name = name.replaceAll(getSeparate(), getReplaceSeparate());
                } else if (getProses() == IS_PROCESS_SHORT_VIEW) {
                    if (name.lastIndexOf(getSeparate()) != -1) {
                        name = name.substring(name.lastIndexOf(getSeparate()) + 1, name.length());
                    }
                }
            case WITH_USE_SWITCH_MERGE_MANUAL:
                break;
        }
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
    }

    public long getMerkId() {
        return merkId;
    }

    public void setMerkId(long merkId) {
        this.merkId = merkId;
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

    public long getDefaultStockUnitId() {
        return defaultStockUnitId;
    }

    public void setDefaultStockUnitId(long defaultSellUnitId) {
        this.defaultStockUnitId = defaultSellUnitId;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public long getDefaultPriceCurrencyId() {
        return defaultPriceCurrencyId;
    }

    public void setDefaultPriceCurrencyId(long defaultPriceCurrencyId) {
        this.defaultPriceCurrencyId = defaultPriceCurrencyId;
    }

    public double getDefaultCost() {
        return defaultCost;
    }

    public void setDefaultCost(double defaultCost) {
        this.defaultCost = defaultCost;
    }

    public long getDefaultCostCurrencyId() {
        return defaultCostCurrencyId;
    }

    public void setDefaultCostCurrencyId(long defaultCostCurrencyId) {
        this.defaultCostCurrencyId = defaultCostCurrencyId;
    }

    public int getDefaultSupplierType() {
        return defaultSupplierType;
    }

    public void setDefaultSupplierType(int defaultSupplierType) {
        this.defaultSupplierType = defaultSupplierType;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public double getPriceType01() {
        return priceType01;
    }

    public void setPriceType01(double priceType01) {
        this.priceType01 = priceType01;
    }

    public double getPriceType02() {
        return priceType02;
    }

    public void setPriceType02(double priceType02) {
        this.priceType02 = priceType02;
    }

    public double getPriceType03() {
        return priceType03;
    }

    public void setPriceType03(double priceType03) {
        this.priceType03 = priceType03;
    }

    public int getMaterialType() {
        return materialType;
    }

    public void setMaterialType(int materialType) {
        this.materialType = materialType;
    }

    /**
     * Getter for property averagePrice.
     *
     * @return Value of property averagePrice.
     *
     */
    public double getAveragePrice() {
        return this.averagePrice;
    }

    /**
     * Setter for property averagePrice.
     *
     * @param averagePrice New value of property averagePrice.
     *
     */
    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    /**
     * Getter for property minimumPoint.
     *
     * @return Value of property minimumPoint.
     *
     */
    public int getMinimumPoint() {
        return this.minimumPoint;
    }

    /**
     * Setter for property minimumPoint.
     *
     * @param minimumPoint New value of property minimumPoint.
     *
     */
    public void setMinimumPoint(int minimumPoint) {
        this.minimumPoint = minimumPoint;
    }

    /**
     * Getter for property requiredSerialNumber.
     *
     * @return Value of property requiredSerialNumber.
     *
     */
    public int getRequiredSerialNumber() {
        return this.requiredSerialNumber;
    }

    /**
     * Setter for property requiredSerialNumber.
     *
     * @param requiredSerialNumber New value of property requiredSerialNumber.
     *
     */
    public void setRequiredSerialNumber(int requiredSerialNumber) {
        this.requiredSerialNumber = requiredSerialNumber;
    }

    /**
     * Getter for property lastUpdate.
     *
     * @return Value of property lastUpdate.
     *
     */
    public Date getLastUpdate() {
        return this.lastUpdate;
    }

    /**
     * Setter for property lastUpdate.
     *
     * @param lastUpdate New value of property lastUpdate.
     *
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * Getter for property processStatus.
     *
     * @return Value of property processStatus.
     *
     */
    public int getProcessStatus() {
        return this.processStatus;
    }

    /**
     * Setter for property processStatus.
     *
     * @param processStatus New value of property processStatus.
     *
     */
    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public String getFullName() {
        String fullName = "";
        Material nameMaterial = new Material();
        Category nameCategory = new Category();
        Merk nameMerk = new Merk();
        SubCategory nameSubCategory = new SubCategory();
        try {
            nameMaterial = PstMaterial.fetchExc(this.getOID());

        } catch (DBException dbe) {
        }
        if (nameMaterial.getOID() > 0) {
            try {
                nameCategory = PstCategory.fetchExc(nameMaterial.getCategoryId());
            } catch (DBException dbe) {
            }
            try {
                nameMerk = PstMerk.fetchExc(nameMaterial.getMerkId());
            } catch (DBException dbe) {
            }
            //nameSubCategory = PstSubCategory.fetchExc (nameMaterial.getSubCategoryId ()); 

        }
        fullName = nameCategory.getName().toUpperCase() + " " + nameMerk.getName().toUpperCase() + " " + nameMaterial.getName().toUpperCase() + " ";
        //System.out.println(fullName);  
        return fullName;
    }

    /**
     * @return the updateDate
     */
    public java.util.Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the lastCostCargo
     */
    public double getLastCostCargo() {
        return lastCostCargo;
    }

    /**
     * @param lastCostCargo the lastCostCargo to set
     */
    public void setLastCostCargo(double lastCostCargo) {
        this.lastCostCargo = lastCostCargo;
    }

    /**
     * @return the editMaterial
     */
    public int getEditMaterial() {
        return editMaterial;
    }

    /**
     * @param editMaterial the editMaterial to set
     */
    public void setEditMaterial(int editMaterial) {
        this.editMaterial = editMaterial;
    }

    /**
     * @return the pointSales
     */
    public int getPointSales() {
        return pointSales;
    }

    /**
     * @param pointSales the pointSales to set
     */
    public void setPointSales(int pointSales) {
        this.pointSales = pointSales;
    }

    /**
     * @return the materialDescription
     */
    public String getMaterialDescription() {
        return materialDescription;
    }

    /**
     * @param materialDescription the materialDescription to set
     */
    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

//    update by Fitra 01-05-2014
    public String getLogDetail(Entity prevDoc) {
        String includePpnWord = "";
        Material prevMat = (Material) prevDoc;
        //DiscountType discountType = null;
        DiscountQtyMapping discountQtyMapping = new DiscountQtyMapping();
        DiscountMapping discountMapping = new DiscountMapping();
        MaterialStock materialStock = new MaterialStock();
        MaterialStock prevMatStock;
        DiscountMapping prevDiscount;
        PriceTypeMapping priceTypeMapping = new PriceTypeMapping();
        PstCategory pstCategory = new PstCategory();

        //kategori
        Category category = new Category();
        Category prevCategory = new Category();
        try {
            if (this.getCategoryId() != 0) {
                category = PstCategory.fetchExc(this.getCategoryId());
            }
            if (prevMat != null) {
                prevCategory = PstCategory.fetchExc(prevMat.getCategoryId());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //sub kategori
        SubCategory subCategory = new SubCategory();
        SubCategory prevSubCategory = new SubCategory();
        try {
            if (this.getSubCategoryId() != 0) {
                subCategory = PstSubCategory.fetchExc(this.getSubCategoryId());
            }
            if (prevMat != null) {
                prevSubCategory = PstSubCategory.fetchExc(prevMat.getSubCategoryId());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //merk
        Merk merk = new Merk();
        Merk prevMerk = new Merk();
        try {
            if (this.getMerkId() != 0) {
                merk = PstMerk.fetchExc(this.getMerkId());
            }
            if (prevMat != null) {
                prevMerk = PstMerk.fetchExc(prevMat.getMerkId());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //unit
        Unit unit = new Unit();
        Unit prevUnit = new Unit();
        try {
            if (this.getDefaultStockUnitId() != 0) {
                unit = PstUnit.fetchExc(this.getDefaultStockUnitId());
            }
            if (prevMat != null) {
                prevUnit = PstUnit.fetchExc(prevMat.getDefaultStockUnitId());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //currency
        CurrencyType currencyType = new CurrencyType();
        CurrencyType prevCurrencyType = new CurrencyType();
        try {
            if (this.getDefaultCostCurrencyId() != 0) {
                currencyType = PstCurrencyType.fetchExc(this.getDefaultCostCurrencyId());
            }
            if (prevMat != null) {
                prevCurrencyType = PstCurrencyType.fetchExc(prevMat.getDefaultCostCurrencyId());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //ksg
        Ksg ksg = new Ksg();
        Ksg prevKsg = new Ksg();
        try {
            if (this.getGondolaCode() != 0) {
                ksg = PstKsg.fetchExc(getGondolaCode());
            }
            if (prevMat != null) {
                prevKsg = PstKsg.fetchExc(prevMat.getGondolaCode());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //supplier
        ContactList supplier;
        ContactList prevSupplier;
        String supplierName = "";
        String prevSupplierName = "";
        try {
            if (this.getSupplierId() != 0) {
                supplier = PstContactList.fetchExc(this.getSupplierId());
                supplierName = supplier.getContactCode() + " - " + supplier.getCompName();
            }
            if (prevMat != null) {
                prevSupplier = PstContactList.fetchExc(prevMat.getSupplierId());
                prevSupplierName = prevSupplier.getContactCode() + " - " + prevSupplier.getCompName();
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //kepemilikan
        ContactList kepemilikan;
        ContactList prevKepemilikan;
        String kepemilikanName = "";
        String prevKepemilikanName = "";
        try {
            if (this.getKepemilikanId() != 0) {
                kepemilikan = PstContactList.fetchExc(this.getKepemilikanId());
                kepemilikanName = kepemilikan.getContactCode() + " - " + kepemilikan.getCompName();
            }
            if (prevMat != null) {
                prevKepemilikan = PstContactList.fetchExc(prevMat.getKepemilikanId());
                prevKepemilikanName = prevKepemilikan.getContactCode() + " - " + prevKepemilikan.getCompName();
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //warna
        Color color = new Color();
        Color prevColor = new Color();
        try {
            if (this.getPosColor() != 0) {
                color = PstColor.fetchExc(this.getPosColor());
            }
            if (prevMat != null) {
                prevColor = PstColor.fetchExc(prevMat.getPosColor());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        //kadar
        Kadar kadar = new Kadar();
        Kadar prevKadar = new Kadar();
        try {
            if (this.getPosKadar() != 0) {
                kadar = PstKadar.fetchExc(this.getPosKadar());
            }
            if (prevMat != null) {
                prevKadar = PstKadar.fetchExc(prevMat.getPosKadar());
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }

        String history = "";
        if (prevMat == null) {
            history += ""
                    + "Group : " + PstMaterial.strMaterialType[0][this.getMaterialType()] + "; "
                    + "Konsinyasi : " + PstMaterial.strTypeCatalogConsigment[0][this.getMatTypeConsig()] + "; "
                    + "SKU / Barcode : " + this.getBarCode() + "; "
                    + "Kategori : " + category.getName() + "; "
                    + "Sub Kategori : " + subCategory.getName() + "; "
                    + "Merk : " + merk.getName() + "; "
                    + "Nama : " + this.getName() + "; "
                    + "Edit Material : " + this.getEditMaterial() + "; "
                    + "Description : " + this.getMaterialDescription() + "; "
                    + "Serial Number : " + PstMaterial.requiredNames[0][this.getRequiredSerialNumber()] + "; "
                    + "Minimum Point : " + this.getMinimumPoint() + "; "
                    + "Point Sales : " + this.getPointSales() + "; "
                    + "Unit Stok : " + unit.getName() + "; "
                    + "Jenis Mata Uang : " + currencyType.getCode() + "; "
                    + "Unit Buy : " + this.getBuyUnitId() + "; "
                    + "Default Cost : " + String.format("%,.2f", this.getDefaultCost()) + "; "
                    + "PPN Terakhir : " + String.format("%,.2f", this.getLastVat()) + "; "
                    + "Ongkos Kirim Terakhir : " + String.format("%,.2f", this.getLastCostCargo()) + "; "
                    + "Harga Beli Terakhir + PPN : " + String.format("%,.2f", this.getCurrBuyPrice()) + "; "
                    + "Profit : " + String.format("%,.2f", this.getProfit()) + "; "
                    + "Nilai Stok : " + String.format("%,.2f", this.getAveragePrice()) + "; "
                    + "Etalase : " + ksg.getCode() + "; "
                    + "Supplier : " + supplierName + "; "
                    + "Kepemilikan : " + kepemilikanName + "; "
                    + "Warna : " + color.getColorCode() + " - " + color.getColorName() + "; "
                    + "Kadar : " + kadar.getKodeKadar() + " - " + kadar.getKadar() + " - " + kadar.getKarat() + "; "
                    + "Tipe Material Item : " + Material.MATERIAL_TYPE_TITLE[this.getMaterialJenisType()] + "; "
                    + "Foto : " + this.getMaterialImage() + "; "
                    + "";
        } else {
            if (prevMat.getMaterialType() != this.getMaterialType()) {
                history += "Group changed from " + PstMaterial.strMaterialType[0][prevMat.getMaterialType()]
                        + " to " + PstMaterial.strMaterialType[0][this.getMaterialType()] + "; ";
            }
            if (prevMat.getMatTypeConsig() != this.getMatTypeConsig()) {
                history += "Konsinyasi changed from " + PstMaterial.strTypeCatalogConsigment[0][prevMat.getMatTypeConsig()]
                        + " to " + PstMaterial.strTypeCatalogConsigment[0][this.getMatTypeConsig()] + "; ";
            }
            if (!prevMat.getBarCode().equals(this.getBarCode())) {
                history += "SKU / Barcode changed from " + prevMat.getBarCode()
                        + " to " + this.getBarCode() + "; ";
            }
            if (prevMat.getCategoryId() != this.getCategoryId()) {
                history += "Kategori changed from " + prevCategory.getName()
                        + " to " + category.getName() + "; ";
            }
            if (prevMat.getSubCategoryId() != this.getSubCategoryId()) {
                history += "Sub kategori changed from " + prevSubCategory.getName()
                        + " to " + subCategory.getName() + "; ";
            }
            if (prevMat.getMerkId() != this.getMerkId()) {
                history += "Merk changed from " + prevMerk.getName()
                        + " to " + merk.getName() + "; ";
            }
            if (!prevMat.getName().equals(this.getName())) {
                history += "Nama changed from " + prevMat.getName()
                        + " to " + this.getName() + "; ";
            }
            if (prevMat.getEditMaterial() != this.getEditMaterial()) {
                history += "Edit Material changed from " + prevMat.getEditMaterial()
                        + " to " + this.getEditMaterial() + "; ";
            }
            if (!prevMat.getMaterialDescription().equals(this.getMaterialDescription())) {
                history += "Description changed from " + prevMat.getMaterialDescription()
                        + " to " + this.getMaterialDescription() + "; ";
            }
            if (prevMat.getRequiredSerialNumber() != this.getRequiredSerialNumber()) {
                history += "Serial Number changed from " + PstMaterial.requiredNames[0][prevMat.getRequiredSerialNumber()]
                        + " to " + PstMaterial.requiredNames[0][this.getRequiredSerialNumber()] + "; ";
            }
            if (prevMat.getMinimumPoint() != this.getMinimumPoint()) {
                history += "Minimum Point changed from " + prevMat.getMinimumPoint()
                        + " to " + this.getMinimumPoint() + "; ";
            }
            if (prevMat.getPointSales() != this.getPointSales()) {
                history += "Point Sales changed from " + prevMat.getPointSales()
                        + " to " + this.getPointSales() + "; ";
            }
            if (prevMat.getDefaultStockUnitId() != this.getDefaultStockUnitId()) {
                history += "Unit Stok changed from " + prevUnit.getName()
                        + " to " + unit.getName() + "; ";
            }
            if (prevMat.getDefaultCostCurrencyId() != this.getDefaultCostCurrencyId()) {
                history += "Jenis Mata Uang changed from " + prevCurrencyType.getCode()
                        + " to " + currencyType.getCode() + "; ";
            }
            if (prevMat.getBuyUnitId() != this.getBuyUnitId()) {
                history += "Unit Buy Uang changed from " + prevMat.getBuyUnitId()
                        + " to " + currencyType.getCode() + "; ";
            }
            if (prevMat.getDefaultCost() != this.getDefaultCost()) {
                history += "Default Cost Uang changed from " + String.format("%,.2f", prevMat.getDefaultCost())
                        + " to " + String.format("%,.2f", this.getDefaultCost()) + "; ";
            }
            if (prevMat.getLastVat() != this.getLastVat()) {
                history += "PPN Terakhir Uang changed from " + String.format("%,.2f", prevMat.getLastVat())
                        + " to " + String.format("%,.2f", this.getLastVat()) + "; ";
            }
            if (prevMat.getLastCostCargo() != this.getLastCostCargo()) {
                history += "Ongkos Kirim Terakhir changed from " + String.format("%,.2f", prevMat.getLastCostCargo())
                        + " to " + String.format("%,.2f", this.getLastCostCargo()) + "; ";
            }
            if (prevMat.getCurrBuyPrice() != this.getCurrBuyPrice()) {
                history += "Harga Beli Terakhir + PPN changed from " + String.format("%,.2f", prevMat.getCurrBuyPrice())
                        + " to " + String.format("%,.2f", this.getCurrBuyPrice()) + "; ";
            }
            if (prevMat.getProfit() != this.getProfit()) {
                history += "Profit changed from " + String.format("%,.2f", prevMat.getProfit())
                        + " to " + String.format("%,.2f", this.getProfit()) + "; ";
            }
            if (prevMat.getAveragePrice() != this.getAveragePrice()) {
                history += "Nilai Stok changed from " + String.format("%,.2f", prevMat.getAveragePrice())
                        + " to " + String.format("%,.2f", this.getAveragePrice()) + "; ";
                try {
                    String sqlInsert = " INSERT INTO pos_material_average_price_history VALUES ('"+this.getOID()+"','"+this.getAveragePrice()+"','"+Formater.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"') ";
                    int result = DBHandler.execUpdate(sqlInsert);
                } catch (Exception exc) {
                    System.out.println(" XXXXX updateAveragePrice : " + exc);
                }
            }
            if (prevMat.getGondolaCode() != this.getGondolaCode()) {
                history += "Etalase changed from " + prevKsg.getCode()
                        + " to " + ksg.getCode() + "; ";
            }
            if (prevMat.getSupplierId() != this.getSupplierId()) {
                history += "Supplier changed from " + prevSupplierName
                        + " to " + supplierName + "; ";
            }
            if (prevMat.getKepemilikanId() != this.getKepemilikanId()) {
                history += "Kepemilikan changed from " + prevKepemilikanName
                        + " to " + kepemilikanName + "; ";
            }
            if (prevMat.getPosColor() != this.getPosColor()) {
                history += "Warna changed from " + prevColor.getColorCode() + " - " + prevColor.getColorName()
                        + " to " + color.getColorCode() + " - " + color.getColorName() + "; ";
            }
            if (prevMat.getPosKadar() != this.getPosKadar()) {
                history += "Kadar changed from " + prevKadar.getKodeKadar() + " - " + prevKadar.getKadar() + " - " + prevKadar.getKarat()
                        + " to " + kadar.getKodeKadar() + " - " + kadar.getKadar() + " - " + kadar.getKarat() + "; ";
            }
            if (prevMat.getMaterialJenisType()!= this.getMaterialJenisType()) {
                history += "Tipe Material Item changed from " + Material.MATERIAL_TYPE_TITLE[prevMat.getMaterialJenisType()]
                        + " to " + Material.MATERIAL_TYPE_TITLE[this.getMaterialJenisType()] + "; ";
            }
            if (!prevMat.getMaterialImage().equals(this.getMaterialImage())) {
                history += "Foto changed from " + prevMat.getMaterialImage()
                        + " to " + this.getMaterialImage() + "; ";
            }
        }

        return history;
    }

    /**
     * @return the MaterialId
     */
    public String getMaterialId() {
        return MaterialId;
    }

    /**
     * @param MaterialId the MaterialId to set
     */
    public void setMaterialId(String MaterialId) {
        this.MaterialId = MaterialId;
    }

    /**
     * @return the Barcode
     */
    public String getBarcode() {
        return Barcode;
    }

    /**
     * @param Barcode the Barcode to set
     */
    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    /**
     * @return the useSellLocation
     */
    public long getUseSellLocation() {
        return useSellLocation;
    }

    /**
     * @param useSellLocation the useSellLocation to set
     */
    public void setUseSellLocation(long useSellLocation) {
        this.useSellLocation = useSellLocation;
    }

    /**
     * @return the gondolaCode
     */
    public long getGondolaCode() {
        return gondolaCode;
    }

    /**
     * @param gondolaCode the gondolaCode to set
     */
    public void setGondolaCode(long gondolaCode) {
        this.gondolaCode = gondolaCode;
    }

    /**
     * @return the gondolaName
     */
    public String getGondolaName() {
        return gondolaName;
    }

    /**
     * @param gondolaName the gondolaName to set
     */
    public void setGondolaName(String gondolaName) {
        this.gondolaName = gondolaName;
    }

    /**
     * @return the kepemilikanId
     */
    public long getKepemilikanId() {
        return kepemilikanId;
    }

    /**
     * @param kepemilikanId the kepemilikanId to set
     */
    public void setKepemilikanId(long kepemilikanId) {
        this.kepemilikanId = kepemilikanId;
    }

    /**
     * @return the posColor
     */
    public long getPosColor() {
        return posColor;
    }

    /**
     * @param posColor the posColor to set
     */
    public void setPosColor(long posColor) {
        this.posColor = posColor;
    }

    /**
     * @return the posKadar
     */
    public long getPosKadar() {
        return posKadar;
    }

    /**
     * @param posKadar the posKadar to set
     */
    public void setPosKadar(long posKadar) {
        this.posKadar = posKadar;
    }

	/**
	 * @return the salesRule
	 */
	public int getSalesRule() {
		return salesRule;
	}

	/**
	 * @param salesRule the salesRule to set
	 */
	public void setSalesRule(int salesRule) {
		this.salesRule = salesRule;
	}

	/**
	 * @return the returnRule
	 */
	public int getReturnRule() {
		return returnRule;
	}

	/**
	 * @param returnRule the returnRule to set
	 */
	public void setReturnRule(int returnRule) {
		this.returnRule = returnRule;
	}

	/**
	 * @return the chainType
	 */
	public int getChainType() {
		return chainType;
	}

	/**
	 * @param chainType the chainType to set
	 */
	public void setChainType(int chainType) {
		this.chainType = chainType;
	}
  /**
   * @return the colorId
   */
  public long getColorId() {
    return colorId;
  }

  /**
   * @param colorId the colorId to set
   */
  public void setColorId(long colorId) {
    this.colorId = colorId;
  }

}
