package com.dimata.posbo.entity.masterdata;

/* package java */
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.PstPriceType;
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MaterialStock extends Entity {

    private long periodeId = 0;
    private long materialUnitId = 0;
    private long locationId = 0;
    private double qty = 0;
    private double qtyMin = 0;
    private double qtyMax = 0;
    private double qtyIn = 0;
    private double qtyOut = 0;
    private double openingQty = 0;
    private double closingQty = 0;
    private double opnameQty = 0;
    private double saleQty = 0;
    private double qtyOptimum = 0; //qty savetyy
    private double qtyCost = 0;
    //-- added by dewok 2017 for nilai berat
    private double berat = 0;
    private double beratIn = 0;
    private double beratOut = 0;
    private double openingBerat = 0;
    private double closingBerat = 0;
    private Date updateDate = null;
    //ADDED BY DEWOK 20190926 FOR STOCK PRODUCTION
    private double qtyProductionCost = 0;
    private double qtyProductionProduct = 0;

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public double getBeratIn() {
        return beratIn;
    }

    public void setBeratIn(double beratIn) {
        this.beratIn = beratIn;
    }

    public double getBeratOut() {
        return beratOut;
    }

    public void setBeratOut(double beratOut) {
        this.beratOut = beratOut;
    }

    public double getOpeningBerat() {
        return openingBerat;
    }

    public void setOpeningBerat(double openingBerat) {
        this.openingBerat = openingBerat;
    }

    public double getClosingBerat() {
        return closingBerat;
    }

    public void setClosingBerat(double closingBerat) {
        this.closingBerat = closingBerat;
    }

    public double getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(double saleQty) {
        this.saleQty = saleQty;
    }

    public long getPeriodeId() {
        return periodeId;
    }

    public double getOpnameQty() {
        return opnameQty;
    }

    public void setOpnameQty(double opnameQty) {
        this.opnameQty = opnameQty;
    }

    public void setPeriodeId(long periodeId) {
        this.periodeId = periodeId;
    }

    public long getMaterialUnitId() {
        return materialUnitId;
    }

    public void setMaterialUnitId(long materialUnitId) {
        this.materialUnitId = materialUnitId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getQtyMin() {
        return qtyMin;
    }

    public void setQtyMin(double qtyMin) {
        this.qtyMin = qtyMin;
    }

    public double getQtyMax() {
        return qtyMax;
    }

    public void setQtyMax(double qtyMax) {
        this.qtyMax = qtyMax;
    }

    public double getQtyIn() {
        return qtyIn;
    }

    public void setQtyIn(double qtyIn) {
        this.qtyIn = qtyIn;
    }

    public double getQtyOut() {
        return qtyOut;
    }

    public void setQtyOut(double qtyOut) {
        this.qtyOut = qtyOut;
    }

    public double getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(double openingQty) {
        this.openingQty = openingQty;
    }

    public double getClosingQty() {
        return closingQty;
    }

    public void setClosingQty(double closingQty) {
        this.closingQty = closingQty;
    }

    /**
     * @return the qtyOptimum
     */
    public double getQtyOptimum() {
        return qtyOptimum;
    }

    /**
     * @param qtyOptimum the qtyOptimum to set
     */
    public void setQtyOptimum(double qtyOptimum) {
        this.qtyOptimum = qtyOptimum;
    }

    // add by fitra anggara yudha 01-05-2014
    public String getLogDetail(Entity prevDoc) {
        String includePpnWord = "";
        MaterialStock prevMaterialStock = (MaterialStock) prevDoc;
        Location location = new Location();
        //Material prevMat = (Material)prevDoc;
        //DiscountQtyMapping discountQtyMapping = new DiscountQtyMapping();
        //DiscountType discountType = null;

//        DiscountType discountType = new DiscountType();
//        DiscountMapping prevDiscountMapping = (DiscountMapping)prevDoc;
        try {
            if (this.getLocationId() != 0) {

                location = PstLocation.fetchExc(getLocationId());

            }
        } catch (Exception exc) {

        }

        return (prevMaterialStock == null || prevMaterialStock.getOID() == 0 || prevMaterialStock.getQtyOptimum() != this.getQtyOptimum() || prevMaterialStock.getQtyMin() != this.getQtyMin()
                ? ("Location: " + location.getName() + " ;") : "")
                + (prevMaterialStock == null || prevMaterialStock.getOID() == 0 || prevMaterialStock.getQtyOptimum() != this.getQtyOptimum()
                        ? ("Qty Optimum saat ini : " + this.getQtyOptimum() + " ;") : "")
                + (prevMaterialStock == null || prevMaterialStock.getOID() == 0 || prevMaterialStock.getQtyOptimum() != this.getQtyOptimum()
                        ? ("Qty Optimum sebelum di update : " + prevMaterialStock.getQtyOptimum() + " ;") : "")
                + //        (prevMaterialStock == null ||  prevMaterialStock.getOID()==0 ||  prevMaterialStock.getQtyMax()!=this.getQtyMax() ?
                //       ("Qty Max: "+ this.getQtyMax() +" ;" ) : "" ) +        
                (prevMaterialStock == null || prevMaterialStock.getOID() == 0 || prevMaterialStock.getQtyMin() != this.getQtyMin()
                        ? ("Qty Min saat ini : " + this.getQtyMin() + " ;") : "")
                + (prevMaterialStock == null || prevMaterialStock.getOID() == 0 || prevMaterialStock.getQtyMin() != this.getQtyMin()
                        ? ("Qty Min Sebelum Di Update: " + prevMaterialStock.getQtyMin() + " ;") : "");

    }

    /**
     * @return the qtyCost
     */
    public double getQtyCost() {
        return qtyCost;
    }

    /**
     * @param qtyCost the qtyCost to set
     */
    public void setQtyCost(double qtyCost) {
        this.qtyCost = qtyCost;
    }

    public double getQtyProductionCost() {
        return qtyProductionCost;
    }

    public void setQtyProductionCost(double qtyProductionCost) {
        this.qtyProductionCost = qtyProductionCost;
    }

    public double getQtyProductionProduct() {
        return qtyProductionProduct;
    }

    public void setQtyProductionProduct(double qtyProductionProduct) {
        this.qtyProductionProduct = qtyProductionProduct;
    }

}
