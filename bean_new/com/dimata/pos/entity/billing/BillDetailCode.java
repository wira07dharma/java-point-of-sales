/*
 * BillDetailCode.java
 *
 * Created on December 18, 2004, 2:33 PM
 */

package com.dimata.pos.entity.billing;

import com.dimata.qdep.entity.Entity;

/* package qdep */
/**
 *
 * @author  wpradnyana
 */
public class BillDetailCode extends Entity
{
    
    /** Creates a new instance of BillDetailCode */
    public BillDetailCode ()
    {
    }
    
    /**
     * Getter for property saleItemId.
     * @return Value of property saleItemId.
     */
    public long getSaleItemId ()
    {
        return saleItemId;
    }
    
    /**
     * Setter for property saleItemId.
     * @param saleItemId New value of property saleItemId.
     */
    public void setSaleItemId (long saleItemId)
    {
        this.saleItemId = saleItemId;
    }
    
    /**
     * Getter for property stockCode.
     * @return Value of property stockCode.
     */
    public java.lang.String getStockCode ()
    {
        return stockCode;
    }
    
    /**
     * Setter for property stockCode.
     * @param stockCode New value of property stockCode.
     */
    public void setStockCode (java.lang.String stockCode)
    {
        this.stockCode = stockCode;
    }
    
    /**
     * Getter for property updateStatus.
     * @return Value of property updateStatus.
     */
    public int getUpdateStatus ()
    {
        return updateStatus;
    }
    
    /**
     * Setter for property updateStatus.
     * @param updateStatus New value of property updateStatus.
     */
    public void setUpdateStatus (int updateStatus)
    {
        this.updateStatus = updateStatus;
    }
    
    private long saleItemId;
    private String stockCode="";
    private int updateStatus=0; 
    private double value =0.0;
    private long dpMaterialItemCodeId; //code dispatch

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the dpMaterialItemCodeId
     */
    public long getDpMaterialItemCodeId() {
        return dpMaterialItemCodeId;
    }

    /**
     * @param dpMaterialItemCodeId the dpMaterialItemCodeId to set
     */
    public void setDpMaterialItemCodeId(long dpMaterialItemCodeId) {
        this.dpMaterialItemCodeId = dpMaterialItemCodeId;
    }
}
