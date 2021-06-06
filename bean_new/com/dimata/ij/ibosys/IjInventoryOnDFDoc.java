/*
 * InventoryValueonDF.java
 *
 * Created on December 29, 2004, 1:04 PM
 */

package com.dimata.ij.ibosys;

// import java core package
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author  Administrator
 * @version 
 */
public class IjInventoryOnDFDoc {

    /** Holds value of property docId. */
    private long docId;
    
    /** Holds value of property docNumber. */
    private String docNumber;
    
    /** Holds value of property docTransDate. */
    private Date docTransDate;
    
    /** Holds value of property docContact. */
    private long docContact;
    
    /** Holds value of property listInventory. */
    private Vector listInventory;
    
    /** Holds value of property dtLastUpdate. */
    private Date dtLastUpdate;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /** Holds value of property dtLastUpdate. */
    private String desc;

    /** Creates new InventoryValueonDF */
    public IjInventoryOnDFDoc() {
    }

    /** Getter for property docId.
     * @return Value of property docId.
     *
     */
    public long getDocId() {
        return this.docId;
    }
    
    /** Setter for property docId.
     * @param docId New value of property docId.
     *
     */
    public void setDocId(long docId) {
        this.docId = docId;
    }
    
    /** Getter for property docNumber.
     * @return Value of property docNumber.
     *
     */
    public String getDocNumber() {
        return this.docNumber;
    }
    
    /** Setter for property docNumber.
     * @param docNumber New value of property docNumber.
     *
     */
    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }
    
    /** Getter for property docTransDate.
     * @return Value of property docTransDate.
     *
     */
    public Date getDocTransDate() {
        return this.docTransDate;
    }
    
    /** Setter for property docTransDate.
     * @param docTransDate New value of property docTransDate.
     *
     */
    public void setDocTransDate(Date docTransDate) {
        this.docTransDate = docTransDate;
    }
    
    /** Getter for property docContact.
     * @return Value of property docContact.
     *
     */
    public long getDocContact() {
        return this.docContact;
    }
    
    /** Setter for property docContact.
     * @param docContact New value of property docContact.
     *
     */
    public void setDocContact(long docContact) {
        this.docContact = docContact;
    }
    
    /** Getter for property listInventory.
     * @return Value of property listInventory.
     *
     */
    public Vector getListInventory() {
        return this.listInventory;
    }
    
    /** Setter for property listInventory.
     * @param listInventory New value of property listInventory.
     *
     */
    public void setListInventory(Vector listInventory) {
        this.listInventory = listInventory;
    }
    
    /** Getter for property dtLastUpdate.
     * @return Value of property dtLastUpdate.
     */
    public Date getDtLastUpdate() {
        return dtLastUpdate;
    }
    
    /** Setter for property dtLastUpdate.
     * @param dtLastUpdate New value of property dtLastUpdate.
     */
    public void setDtLastUpdate(Date dtLastUpdate) {
        this.dtLastUpdate = dtLastUpdate;
    }
    
}
