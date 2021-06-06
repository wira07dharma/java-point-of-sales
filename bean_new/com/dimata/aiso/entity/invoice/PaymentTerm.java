/*
 * PaymentTerm.java
 *
 * Created on November 12, 2007, 2:18 PM
 */

package com.dimata.aiso.entity.invoice;

/**
 *
 * @author  dwi
 */
/* import java */
import java.util.*;
import java.util.Date;

/* import qdep */
import com.dimata.qdep.entity.*;

public class PaymentTerm extends Entity{
    
    /**
     * Holds value of property amount.
     */
    private double amount = 0;    
  
    /**
     * Holds value of property note.
     */
    private String note = "";
    
    /**
     * Holds value of property type.
     */
    private int type = 0;
    
    /**
     * Holds value of property planDate.
     */
    private Date planDate = new Date();
    
    /**
     * Holds value of property invoiceId.
     */
    private long invoiceId = 0;
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return this.amount;
    }    
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * Getter for property note.
     * @return Value of property note.
     */
    public String getNote() {
        return this.note;
    }
    
    /**
     * Setter for property note.
     * @param note New value of property note.
     */
    public void setNote(String note) {
        this.note = note;
    }
    
    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public int getType() {
        return this.type;
    }
    
    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * Getter for property planDate.
     * @return Value of property planDate.
     */
    public Date getPlanDate() {
        return this.planDate;
    }
    
    /**
     * Setter for property planDate.
     * @param type New value of property planDate.
     */
    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }
    
    /**
     * Getter for property invoiceId.
     * @return Value of property invoiceId.
     */
    public long getInvoiceId() {
        return this.invoiceId;
    }
    
    /**
     * Setter for property invoiceId.
     * @param invoiceId New value of property invoiceId.
     */
    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }
    
}
