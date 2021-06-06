/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.posbo.entity.masterdata;

/* package java */

import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatVendorPrice extends Entity {

    private long materialId;
    private long buyingUnitId;
    private long vendorId;
    private long priceCurrency;
    private String description = "";
    private String vendorPriceCode = "";
    private String vendorPriceBarcode = "";
    private double orgBuyingPrice;
    private double lastDiscount;
    private double lastVat;
    private double currBuyingPrice;
    private double lastCostCargo;
    private String buyingUnitName="";

    /* add discount % from receive
     * By Mirahu
     */
    private double lastDiscount1;
    private double lastDiscount2;


    public double getLastCostCargo() {
        return lastCostCargo;
    }

    public void setLastCostCargo(double lastCostCargo) {
        this.lastCostCargo = lastCostCargo;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public long getBuyingUnitId() {
        return buyingUnitId;
    }

    public void setBuyingUnitId(long buyingUnitId) {
        this.buyingUnitId = buyingUnitId;
    }

    public long getVendorId() {
        return vendorId;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorPriceCode() {
        return vendorPriceCode;
    }

    public void setVendorPriceCode(String vendorPriceCode) {
        this.vendorPriceCode = vendorPriceCode;
    }

    public String getVendorPriceBarcode() {
        return vendorPriceBarcode;
    }

    public void setVendorPriceBarcode(String vendorPriceBarcode) {
        this.vendorPriceBarcode = vendorPriceBarcode;
    }

    public long getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(long priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public double getOrgBuyingPrice() {
        return orgBuyingPrice;
    }

    public void setOrgBuyingPrice(double orgBuyingPrice) {
        this.orgBuyingPrice = orgBuyingPrice;
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

    public double getCurrBuyingPrice() {
        return currBuyingPrice;
    }

    public void setCurrBuyingPrice(double currBuyingPrice) {
        this.currBuyingPrice = currBuyingPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the lastDiscount1
     */
    public double getLastDiscount1() {
        return lastDiscount1;
    }

    /**
     * @param lastDiscount1 the lastDiscount1 to set
     */
    public void setLastDiscount1(double lastDiscount1) {
        this.lastDiscount1 = lastDiscount1;
    }

    /**
     * @return the lastDiscount2
     */
    public double getLastDiscount2() {
        return lastDiscount2;
    }

    /**
     * @param lastDiscount2 the lastDiscount2 to set
     */
    public void setLastDiscount2(double lastDiscount2) {
        this.lastDiscount2 = lastDiscount2;
    }

    /**
     * @return the buyingUnitName
     */
    public String getBuyingUnitName() {
        return buyingUnitName;
    }

    /**
     * @param buyingUnitName the buyingUnitName to set
     */
    public void setBuyingUnitName(String buyingUnitName) {
        this.buyingUnitName = buyingUnitName;
    }
}
