/*
 * SaleModel.java
 *
 * Created on February 26, 2005, 1:40 PM
 */

package com.dimata.ObjLink.BOCashier;

import java.util.Vector;


/**
 *
 * @author  wpradnyana
 */
public class SaleModel
{
    
    /** Creates a new instance of SaleModel */
    public SaleModel ()
    {
    }
    
    /**
     * Getter for property mainBill.
     * @return Value of property mainBill.
     */
    public MainBillLink getMainBill ()
    {
        if(mainBill==null){
            mainBill = new MainBillLink();
        }
        return mainBill;
    }
    
    /**
     * Setter for property mainBill.
     * @param mainBill New value of property mainBill.
     */
    public void setMainBill (MainBillLink mainBill)
    {
        this.mainBill = mainBill;
    }
    
    /**
     * Getter for property detailBill.
     * @return Value of property detailBill.
     */
    public java.util.Vector getDetailBill ()
    {
        if(detailBill==null){
            detailBill = new Vector();  
        }
        return detailBill;
    }
    
    /**
     * Setter for property detailBill.
     * @param detailBill New value of property detailBill.
     */
    public void setDetailBill (java.util.Vector detailBill)
    {
        this.detailBill = detailBill;
    }
    
    /**
     * Getter for property paymentBill.
     * @return Value of property paymentBill.
     */
    public Vector getPaymentBill ()
    {
        if(vctPaymentBill==null){
            vctPaymentBill = new Vector();
        }
        return vctPaymentBill;
    }
    
    /**
     * Setter for property paymentBill.
     * @param paymentBill New value of property paymentBill.
     */
    public void setPaymentBill (Vector vctPaymentBill)
    {
        this.vctPaymentBill = vctPaymentBill;
    }
    
    /**
     * Getter for property customer.
     * @return Value of property customer.
     */
    public com.dimata.ObjLink.BOCashier.CustomerLink getCustomer ()
    {
        return customer;
    }
    
    /**
     * Setter for property customer.
     * @param customer New value of property customer.
     */
    public void setCustomer (com.dimata.ObjLink.BOCashier.CustomerLink customer)
    {
        this.customer = customer;
    }
    
    private MainBillLink mainBill;
    private Vector detailBill;
    private CustomerLink customer;  
    private Vector vctPaymentBill;
    
}
