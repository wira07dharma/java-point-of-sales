/*
 * PaymentValueonLGR.java
 *
 * Created on December 29, 2004, 1:05 PM
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
public class IjPaymentOnLGRDoc {

    /** Holds value of property docId. */
    private long docId;
    
    /** Holds value of property docNumber. */
    private String docNumber;
    
    /** Holds value of property docTransDate. */
    private Date docTransDate;
    
    /** Holds value of property docTransCurrency. */
    private long docTransCurrency;
    
    /** Holds value of property docContact. */
    private long docContact;
    
    /** Holds value of property listPayment. */
    private Vector listPayment;
    
    /** Holds value of property refLGRNumber. */
    private String refLGRNumber;
    
    /** Holds value of property dtLastUpdate. */
    private Date dtLastUpdate;
    
    /** Creates new PaymentValueonLGR */
    public IjPaymentOnLGRDoc() {
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
    
    /** Getter for property docTransCurrency.
     * @return Value of property docTransCurrency.
     *
     */
    public long getDocTransCurrency() {
        return this.docTransCurrency;
    }
    
    /** Setter for property docTransCurrency.
     * @param docTransCurrency New value of property docTransCurrency.
     *
     */
    public void setDocTransCurrency(long docTransCurrency) {
        this.docTransCurrency = docTransCurrency;
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
    
    /** Getter for property listPayment.
     * @return Value of property listPayment.
     *
     */
    public Vector getListPayment() {
        return this.listPayment;
    }
    
    /** Setter for property listPayment.
     * @param listPayment New value of property listPayment.
     *
     */
    public void setListPayment(Vector listPayment) {
        this.listPayment = listPayment;
    }
    
    /** Getter for property refLGRNumber.
     * @return Value of property refLGRNumber.
     */
    public String getRefLGRNumber() {
        return refLGRNumber;
    }
    
    /** Setter for property refLGRNumber.
     * @param refLGRNumber New value of property refLGRNumber.
     */
    public void setRefLGRNumber(String refLGRNumber) {
        this.refLGRNumber = refLGRNumber;
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
