/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.purchasing;

import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class PurchaseRequestItem extends Entity implements I_LogHistory{
    private long purchaseOrderId = 0;
    private long materialId = 0;
    private long unitId = 0;
    private double quantity = 0;
    private double currentStock=0.0;
    private double minimStock=0.0;
    private int approvalStatus=0;
    private String note="";
    private long supplierId=0;
    private double priceBuying=0.0;
    private long unitRequestId=0;
    private String supplierName="";
    private double totalPrice=0.0;
    private int termPurchaseRequest=0;
    
    /**
     * @return the purchaseOrderId
     */
    public long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    /**
     * @param purchaseOrderId the purchaseOrderId to set
     */
    public void setPurchaseOrderId(long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    /**
     * @return the materialId
     */
    public long getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    /**
     * @return the unitId
     */
    public long getUnitId() {
        return unitId;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    /**
     * @return the quantity
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

   public String getLogDetail(Entity prevDocItem) {
        PurchaseRequestItem prevPrItem = (PurchaseRequestItem)prevDocItem;
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

        return  "SKU : " + material.getSku()+
                (prevPrItem == null ||  prevPrItem.getOID()==0 || prevPrItem.getMaterialId() == 0 ||  prevPrItem.getMaterialId() != this.getMaterialId() ?
                (" Nama Barang : " + material.getName()) : "") +

                (prevPrItem == null ||  prevPrItem.getOID()==0 || prevPrItem.getQuantity() == 0 ||  prevPrItem.getQuantity() != this.getQuantity() ?
                ( " QTY : " + this.getQuantity() ) : "");
    }

    /**
     * @return the currentStock
     */
    public double getCurrentStock() {
        return currentStock;
    }

    /**
     * @param currentStock the currentStock to set
     */
    public void setCurrentStock(double currentStock) {
        this.currentStock = currentStock;
    }

    /**
     * @return the minimStock
     */
    public double getMinimStock() {
        return minimStock;
    }

    /**
     * @param minimStock the minimStock to set
     */
    public void setMinimStock(double minimStock) {
        this.minimStock = minimStock;
    }

    /**
     * @return the approvalStatus
     */
    public int getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * @param approvalStatus the approvalStatus to set
     */
    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the supplierId
     */
    public long getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the priceBuying
     */
    public double getPriceBuying() {
        return priceBuying;
    }

    /**
     * @param priceBuying the priceBuying to set
     */
    public void setPriceBuying(double priceBuying) {
        this.priceBuying = priceBuying;
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
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * @return the totalPrice
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the termPurchaseRequest
     */
    public int getTermPurchaseRequest() {
        return termPurchaseRequest;
    }

    /**
     * @param termPurchaseRequest the termPurchaseRequest to set
     */
    public void setTermPurchaseRequest(int termPurchaseRequest) {
        this.termPurchaseRequest = termPurchaseRequest;
    }

}
