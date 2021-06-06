/*
 * DetailBill.java
 *
 * Created on February 26, 2005, 1:42 PM
 */

package com.dimata.ObjLink.BOCashier;

import java.util.*;
/**
 *
 * @author  wpradnyana
 */
public class DetailBillLink
{
    
    /**
     * Holds value of property saleDetailId.
     */
    private long saleDetailId;
    
    /**
     * Holds value of property mainBillId.
     */
    private long mainBillId;
    
    /**
     * Holds value of property cost.
     */
    private double cost;
    
    /**
     * Holds value of property disc.
     */
    private double disc;
    
    /**
     * Holds value of property discPct.
     */
    private double discPct;
    
    /**
     * Holds value of property itemName.
     */
    private String itemName;
    
    /**
     * Holds value of property itemPrice.
     */
    private double itemPrice;
    
    /**
     * Holds value of property locationId.
     */
    private long locationId;
    
    /**
     * Holds value of property soldQty.
     */
    private double soldQty;
    
    /**
     * Holds value of property itemCode.
     */
    private String itemCode;
    
    /**
     * Holds value of property unitId.
     */
    private long unitId;
    
    /**
     * Holds value of property totalSold.
     */
    private double totalSold;    
    private long itemId;
    
    /** Creates a new instance of DetailBill */
    public DetailBillLink ()
    {
    }
    
    /**
     * Getter for property saleDetailId.
     * @return Value of property saleDetailId.
     */
    public long getSaleDetailId ()
    {
        return this.saleDetailId;
    }    
    
    /**
     * Setter for property saleDetailId.
     * @param saleDetailId New value of property saleDetailId.
     */
    public void setSaleDetailId (long saleDetailId)
    {
        this.saleDetailId = saleDetailId;
    }
    
    /**
     * Getter for property mainBillId.
     * @return Value of property mainBillId.
     */
    public long getMainBillId ()
    {
        return this.mainBillId;
    }
    
    /**
     * Setter for property mainBillId.
     * @param mainBillId New value of property mainBillId.
     */
    public void setMainBillId (long mainBillId)
    {
        this.mainBillId = mainBillId;
    }
    
    /**
     * Getter for property cost.
     * @return Value of property cost.
     */
    public double getCost ()
    {
        return this.cost;
    }
    
    /**
     * Setter for property cost.
     * @param cost New value of property cost.
     */
    public void setCost (double cost)
    {
        this.cost = cost;
    }
    
    /**
     * Getter for property disc.
     * @return Value of property disc.
     */
    public double getDisc ()
    {
        return this.disc;
    }
    
    /**
     * Setter for property disc.
     * @param disc New value of property disc.
     */
    public void setDisc (double disc)
    {
        this.disc = disc;
    }
    
    /**
     * Getter for property discPct.
     * @return Value of property discPct.
     */
    public double getDiscPct ()
    {
        return this.discPct;
    }
    
    /**
     * Setter for property discPct.
     * @param discPct New value of property discPct.
     */
    public void setDiscPct (double discPct)
    {
        this.discPct = discPct;
    }
    
    /**
     * Getter for property itemName.
     * @return Value of property itemName.
     */
    public String getItemName ()
    {
        return this.itemName;
    }
    
    /**
     * Setter for property itemName.
     * @param itemName New value of property itemName.
     */
    public void setItemName (String itemName)
    {
        this.itemName = itemName;
    }
    
    /**
     * Getter for property itemPrice.
     * @return Value of property itemPrice.
     */
    public double getItemPrice ()
    {
        return this.itemPrice;
    }
    
    /**
     * Setter for property itemPrice.
     * @param itemPrice New value of property itemPrice.
     */
    public void setItemPrice (double itemPrice)
    {
        this.itemPrice = itemPrice;
    }
    
    /**
     * Getter for property locationId.
     * @return Value of property locationId.
     */
    public long getLocationId ()
    {
        return this.locationId;
    }
    
    /**
     * Setter for property locationId.
     * @param locationId New value of property locationId.
     */
    public void setLocationId (long locationId)
    {
        this.locationId = locationId;
    }
    
    /**
     * Getter for property soldQty.
     * @return Value of property soldQty.
     */
    public double getSoldQty ()
    {
        return this.soldQty;
    }
    
    /**
     * Setter for property soldQty.
     * @param soldQty New value of property soldQty.
     */
    public void setSoldQty (double soldQty)
    {
        this.soldQty = soldQty;
    }
    
    /**
     * Getter for property itemCode.
     * @return Value of property itemCode.
     */
    public String getItemCode ()
    {
        return this.itemCode;
    }
    
    /**
     * Setter for property itemCode.
     * @param itemCode New value of property itemCode.
     */
    public void setItemCode (String itemCode)
    {
        this.itemCode = itemCode;
    }
    
    /**
     * Getter for property unitId.
     * @return Value of property unitId.
     */
    public long getUnitId ()
    {
        return this.unitId;
    }
    
    /**
     * Setter for property unitId.
     * @param unitId New value of property unitId.
     */
    public void setUnitId (long unitId)
    {
        this.unitId = unitId;
    }
    
    /**
     * Getter for property totalSold.
     * @return Value of property totalSold.
     */
    public double getTotalSold ()
    {
        return this.totalSold;
    }
    
    /**
     * Setter for property totalSold.
     * @param totalSold New value of property totalSold.
     */
    public void setTotalSold (double totalSold)
    {
        this.totalSold = totalSold;
    }    

    public long getItemId(){ return itemId; }    

    public void setItemId(long itemId){ this.itemId = itemId; }
}
