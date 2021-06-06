/*
 * SalesValueonInvoice.java
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
public class IjSalesOnInvDoc {

    /** Holds value of property docId. */
    private long docId;
    
    /** Holds value of property docNumber. */
    private String docNumber;
    
    /** Holds value of property docTransDate. */
    private Date docTransDate;
    
    /** Holds value of property docTransCurrency. */
    private long docTransCurrency;
    
    /** Holds value of property docLocation. */
    private long docLocation;
    
    /** Holds value of property docContact. */
    private long docContact;
    
    /** Holds value of property docDiscount. */
    private double docDiscount;
    
    /** Holds value of property docTax. */
    private double docTax;
    
    /** Holds value of property docOtherCost. */
    private double docOtherCost;
    
    /** Holds value of property listSalesValue. */
    private Vector listSalesValue;
    
    /** Holds value of property listDPDeduction. */
    private Vector listDPDeduction;
    
    /** Holds value of property docSaleType. */
    private int docSaleType;
    
    /** Holds value of property refSONumber. */
    private String refSONumber;
    
    /** Holds value of property docDueDate. */
    private Date docDueDate;
    
    /** Holds value of property dtLastUpdate. */
    private Date dtLastUpdate;
    
    /** Holds value of property dtLastUpdate. */
    private int transactionType;

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }
    
    /** Holds value of property listPayment. */
    private Vector listPayment;
    
    /** Creates new SalesValueonInvoice */
    public IjSalesOnInvDoc() {
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
    
    /** Getter for property docLocation.
     * @return Value of property docLocation.
     *
     */
    public long getDocLocation() {
        return this.docLocation;
    }
    
    /** Setter for property docLocation.
     * @param docLocation New value of property docLocation.
     *
     */
    public void setDocLocation(long docLocation) {
        this.docLocation = docLocation;
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
    
    /** Getter for property docDiscount.
     * @return Value of property docDiscount.
     *
     */
    public double getDocDiscount() {
        return this.docDiscount;
    }
    
    /** Setter for property docDiscount.
     * @param docDiscount New value of property docDiscount.
     *
     */
    public void setDocDiscount(double docDiscount) {
        this.docDiscount = docDiscount;
    }
    
    /** Getter for property docTax.
     * @return Value of property docTax.
     *
     */
    public double getDocTax() {
        return this.docTax;
    }
    
    /** Setter for property docTax.
     * @param docTax New value of property docTax.
     *
     */
    public void setDocTax(double docTax) {
        this.docTax = docTax;
    }
    
    /** Getter for property docOtherCost.
     * @return Value of property docOtherCost.
     *
     */
    public double getDocOtherCost() {
        return this.docOtherCost;
    }
    
    /** Setter for property docOtherCost.
     * @param docOtherCost New value of property docOtherCost.
     *
     */
    public void setDocOtherCost(double docOtherCost) {
        this.docOtherCost = docOtherCost;
    }
    
    /** Getter for property listSalesValue.
     * @return Value of property listSalesValue.
     *
     */
    public Vector getListSalesValue() {
        return this.listSalesValue;
    }
    
    /** Setter for property listSalesValue.
     * @param listSalesValue New value of property listSalesValue.
     *
     */
    public void setListSalesValue(Vector listSalesValue) {
        this.listSalesValue = listSalesValue;
    }
    
    /** Getter for property listDPDeduction.
     * @return Value of property listDPDeduction.
     *
     */
    public Vector getListDPDeduction() {
        return this.listDPDeduction;
    }
    
    /** Setter for property listDPDeduction.
     * @param listDPDeduction New value of property listDPDeduction.
     *
     */
    public void setListDPDeduction(Vector listDPDeduction) {
        this.listDPDeduction = listDPDeduction;
    }
    
    /** Getter for property saleType.
     * @return Value of property saleType.
     *
     */
    public int getDocSaleType() {
        return this.docSaleType;
    }
    
    /** Setter for property saleType.
     * @param saleType New value of property saleType.
     *
     */
    public void setDocSaleType(int docSaleType) {
        this.docSaleType = docSaleType;
    }
    
    /** Getter for property refSONumber.
     * @return Value of property refSONumber.
     *
     */
    public String getRefSONumber() {
        return this.refSONumber;
    }
    
    /** Setter for property refSONumber.
     * @param refSONumber New value of property refSONumber.
     *
     */
    public void setRefSONumber(String refSONumber) {
        this.refSONumber = refSONumber;
    }
    
    /** Getter for property docDueDate.
     * @return Value of property docDueDate.
     *
     */
    public Date getDocDueDate() {
        return this.docDueDate;
    }
    
    /** Setter for property docDueDate.
     * @param docDueDate New value of property docDueDate.
     *
     */
    public void setDocDueDate(Date docDueDate) {
        this.docDueDate = docDueDate;
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
    
}
