/*
 * GiftItem.java
 *
 * Created on January 10, 2005, 2:49 PM
 */

package com.dimata.pos.cashier;

/**
 *
 * @author  pulantara
 */
public class GiftItem {
    
    /** Creates a new instance of GiftItem */
    public GiftItem() {
    }
    
    /**
     * Getter for property code.
     * @return Value of property code.
     */
    public java.lang.String getCode() {
        return code;
    }
    
    /**
     * Setter for property code.
     * @param code New value of property code.
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /**
     * Getter for property point.
     * @return Value of property point.
     */
    public int getPoint() {
        return point;
    }
    
    /**
     * Setter for property point.
     * @param point New value of property point.
     */
    public void setPoint(int point) {
        this.point = point;
    }
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * Getter for property serialCode.
     * @return Value of property serialCode.
     */
    public String getSerialCode() {
        return serialCode;
    }
    
    /**
     * Setter for property serialCode.
     * @param serialCode New value of property serialCode.
     */
    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }
    
    //Item Code
    private String code = "";
    //Item Name
    private String name = "";
    //Item Point
    private int point = 0;
    //amount
    private double amount = 0;
    //serial code
    private String serialCode = "";
    
}
