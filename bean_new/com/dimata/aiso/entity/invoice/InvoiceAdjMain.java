/*
 * InvoiceAdjMain.java
 *
 * Created on November 12, 2007, 2:14 PM
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

public class InvoiceAdjMain extends Entity{
    
    /**
     * Holds value of property docNumber.
     */
    private String docNumber = "";    
   
    /**
     * Holds value of property note.
     */
    private String note = "";
    
    /**
     * Holds value of property status.
     */
    private int status = 0;
    
    /**
     * Holds value of property invoiceId.
     */
    private long invoiceId = 0;
    
    /**
     * Holds value of property numberCounter.
     */
    private int numberCounter = 0;
    
    /**
     * Holds value of property reference.
     */
    private String reference = "";
    
    /**
     * Holds value of property type.
     */
    private int type = 0;
    
    /**
     * Holds value of property date.
     */
    private Date date = new Date();
    
    /**
     * Getter for property docNumber.
     * @return Value of property docNumber.
     */
    public String getDocNumber() {
        return this.docNumber;
    }    
    
    /**
     * Setter for property docNumber.
     * @param docNumber New value of property docNumber.
     */
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
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
     * Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return this.status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
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
    
    /**
     * Getter for property numberCounter.
     * @return Value of property numberCounter.
     */
    public int getNumberCounter() {
        return this.numberCounter;
    }
    
    /**
     * Setter for property numberCounter.
     * @param numberCounter New value of property numberCounter.
     */
    public void setNumberCounter(int numberCounter) {
        this.numberCounter = numberCounter;
    }
    
    /**
     * Getter for property reference.
     * @return Value of property reference.
     */
    public String getReference() {
        return this.reference;
    }
    
    /**
     * Setter for property reference.
     * @param reference New value of property reference.
     */
    public void setReference(String reference) {
        this.reference = reference;
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
     * Getter for property date.
     * @return Value of property date.
     */
    public Date getDate() {
        return this.date;
    }
    
    /**
     * Setter for property date.
     * @param type New value of property date.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
}

