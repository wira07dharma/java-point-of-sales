package com.dimata.posbo.entity.purchasing;

/* package java */

import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;

/* package qdep */
import com.dimata.qdep.entity.*;

public class PurchaseOrderItem extends Entity implements I_LogHistory{
    
    private long purchaseOrderId = 0;
    private long materialId = 0;
    private long unitId = 0;
    private double price = 0.00;
    private long currencyId = 0;
    private double quantity = 0;
    private double discount = 0.00;
    private double total = 0.00;
    private double residuQty = 0.00;
    private double orgBuyingPrice = 0.00;
    private double discount1 = 0.00;
    private double discount2 = 0.00;
    private double discNominal = 0.00;
    private double curBuyingPrice = 0.00;
    
    private long unitRequestId=0;
    private double qtyRequest=0.0;
    private double priceKonv=0.0;
    
    private double qtyInputStock=0.0;
    
    private int bonus=0;
    
    private double inputCurrentBonus=0.0;
    
    public double getResiduQty() {
        return residuQty;
    }
    
    public void setResiduQty(double residuQty) {
        this.residuQty = residuQty;
    }
    
    // discNominal
    public double getCurBuyingPrice() {
        return curBuyingPrice;
    }
    
    public void setCurBuyingPrice(double curBuyingPrice) {
        this.curBuyingPrice = curBuyingPrice;
    }
    
    // discNominal
    public double getDiscNominal() {
        return discNominal;
    }
    
    public void setDiscNominal(double discNominal) {
        this.discNominal = discNominal;
    }
    
    // discount 1
    public double getDiscount1() {
        return discount1;
    }
    
    public void setDiscount1(double discount1) {
        this.discount1 = discount1;
    }
    
    // discount 2
    public double getDiscount2() {
        return discount2;
    }
    
    public void setDiscount2(double discount2) {
        this.discount2 = discount2;
    }
    
    // buying price
    public double getOrgBuyingPrice() {
        return orgBuyingPrice;
    }
    
    public void setOrgBuyingPrice(double orgBuyingPrice) {
        this.orgBuyingPrice = orgBuyingPrice;
    }
    
    public long getPurchaseOrderId() {
        return purchaseOrderId;
    }
    
    public void setPurchaseOrderId(long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }
    
    public long getMaterialId() {
        return materialId;
    }
    
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }
    
    public long getUnitId() {
        return unitId;
    }
    
    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public long getCurrencyId() {
        return currencyId;
    }
    
    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public double getDiscount() {
        return discount;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    /**
     * Fungsi Untuk menginsertkan Field Detail ke dalam tabel pos_purchase_order_item
     */
    public String getLogDetail(Entity prevDocItem) {
        PurchaseOrderItem prevPoItem = (PurchaseOrderItem)prevDocItem;
        Material material = null;
        try
        {
            if(this!=null && getMaterialId()!=0 )
            {
                material = PstMaterial.fetchExc(getMaterialId());
            }
        }
        catch(Exception e)
        {
        }
       /* double xx = prevPoItem.getPrice();
        double yy =  this.getPrice();
        System.out.print(yy+" dan "+xx);*/
        return  "SKU : " + material.getSku()+

                (prevPoItem == null ||  prevPoItem.getOID()==0 || prevPoItem.getMaterialId() == 0 ||  prevPoItem.getMaterialId() != this.getMaterialId() ?
                (" Nama Barang : " + material.getName()) : "") +

                (prevPoItem == null ||  prevPoItem.getOID()==0 || prevPoItem.getQuantity() == 0 ||  prevPoItem.getQuantity() != this.getQuantity() ?
                ( " QTY : " + this.getQuantity() ) : "")+

                (prevPoItem == null ||  prevPoItem.getOID()==0 || prevPoItem.getPrice() == 0 ||  prevPoItem.getPrice() != this.getPrice() ?
                ( " Harga Beli : " + this.getPrice() ) : "") +

                (prevPoItem == null ||  prevPoItem.getOID()==0 || prevPoItem.getDiscount1() != this.getDiscount1() ?
                ( " Diskon 1 : " + this.getDiscount1() ) : "" ) +

                (prevPoItem == null ||  prevPoItem.getOID()==0 || prevPoItem.getDiscount2() != this.getDiscount2() ?
                ( " Diskon 2 : " + this.getDiscount2() ) : "" ) +

                (prevPoItem == null ||  prevPoItem.getOID()==0 || prevPoItem.getDiscNominal() != this.getDiscNominal() ?
                ( " Diskon Nominal : " + this.getDiscNominal() ) : "" ) +

                (prevPoItem == null ||  prevPoItem.getOID()==0 || prevPoItem.getCurBuyingPrice() == 0 ||  prevPoItem.getCurBuyingPrice() != this.getCurBuyingPrice() ?
                ( " Netto Harga Beli : " + getCurBuyingPrice() ) : "" );
    }

    /**
     * @return the unitRequestId
     */
    public long getUnitRequestId() {
        return unitRequestId;
    }

    /**
     * @param unitRequestId the unitRequestId to set
     */
    public void setUnitRequestId(long unitRequestId) {
        this.unitRequestId = unitRequestId;
    }

    /**
     * @return the qtyRequest
     */
    public double getQtyRequest() {
        return qtyRequest;
    }

    /**
     * @param qtyRequest the qtyRequest to set
     */
    public void setQtyRequest(double qtyRequest) {
        this.qtyRequest = qtyRequest;
    }

    /**
     * @return the priceKonv
     */
    public double getPriceKonv() {
        return priceKonv;
    }

    /**
     * @param priceKonv the priceKonv to set
     */
    public void setPriceKonv(double priceKonv) {
        this.priceKonv = priceKonv;
    }

    /**
     * @return the bonus
     */
    public int getBonus() {
        return bonus;
    }

    /**
     * @param bonus the bonus to set
     */
    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    /**
     * @return the qtyInputStock
     */
    public double getQtyInputStock() {
        return qtyInputStock;
    }

    /**
     * @param qtyInputStock the qtyInputStock to set
     */
    public void setQtyInputStock(double qtyInputStock) {
        this.qtyInputStock = qtyInputStock;
    }

    /**
     * @return the inputCurrentBonus
     */
    public double getInputCurrentBonus() {
        return inputCurrentBonus;
    }

    /**
     * @param inputCurrentBonus the inputCurrentBonus to set
     */
    public void setInputCurrentBonus(double inputCurrentBonus) {
        this.inputCurrentBonus = inputCurrentBonus;
    }
}
