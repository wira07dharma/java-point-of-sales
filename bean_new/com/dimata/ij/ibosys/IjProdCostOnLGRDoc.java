/*
 * ProductionCostValueOnLGR.java
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
public class IjProdCostOnLGRDoc {

    /** Holds value of property docId. */  
    private long docId;
    
    /** Holds value of property docNumber. */
    private String docNumber;
    
    /** Holds value of property docTransDate. */
    private Date docTransDate;
    
    /** Holds value of property docLocation. */
    private long docLocation;
    
    /** Holds value of property docContact. */
    private long docContact;
    
    /** Holds value of property docTransCurrency. */
    private long docTransCurrency;
    
    /** Holds value of property docDueDate. */
    private Date docDueDate;
    
    /** Holds value of property docTax. */
    private double docTax;
    
    /** Holds value of property listProdCost. */
    private Vector listProdCost;
    
    /** Holds value of property listDPDeduction. */
    private Vector listDPDeduction;
    
    /** Holds value of property docSaleType. */
    private int docSaleType;
    
    /** Holds value of property docDiscount. */
    private double docDiscount;
    
    /** Holds value of property refPdONumber. */
    private String refPdONumber;
    
    /** Holds value of property refDfNumber. */
    private String refDfNumber;
    
    /** Holds value of property listInventory. */
    private Vector listInventory;
    
    /** Holds value of property dtLastUpdate. */
    private Date dtLastUpdate;
    
    /** Creates new ProductionCostValueOnLGR */
    public IjProdCostOnLGRDoc() {
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
    
    /** Getter for property docDiscount.
     * @return Value of property docDiscount.
     *
     */
    public Date getDocDueDate() {
        return this.docDueDate;
    }
    
    /** Setter for property docDiscount.
     * @param docDiscount New value of property docDiscount.
     *
     */
    public void setDocDueDate(Date docDueDate) {
        this.docDueDate = docDueDate;
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
    
    /** Getter for property docPurchValue.
     * @return Value of property docPurchValue.
     *
     */
    public Vector getListProdCost() {
        return this.listProdCost;
    }
    
    /** Setter for property docPurchValue.
     * @param docPurchValue New value of property docPurchValue.
     *
     */
    public void setListProdCost(Vector listProdCost) {
        this.listProdCost = listProdCost;
    }
    
    /** Getter for property docDPDeduction.
     * @return Value of property docDPDeduction.
     *
     */
    public Vector getListDPDeduction() {
        return this.listDPDeduction;
    }
    
    /** Setter for property docDPDeduction.
     * @param docDPDeduction New value of property docDPDeduction.
     *
     */
    public void setListDPDeduction(Vector listDPDeduction) {
        this.listDPDeduction = listDPDeduction;
    }    
    
    /** Getter for property docSaleType.
     * @return Value of property docSaleType.
     *
     */
    public int getDocSaleType() {
        return this.docSaleType;
    }
    
    /** Setter for property docSaleType.
     * @param docSaleType New value of property docSaleType.
     *
     */
    public void setDocSaleType(int docSaleType) {
        this.docSaleType = docSaleType;
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
    
    /** Getter for property refPdONumber.
     * @return Value of property refPdONumber.
     *
     */
    public String getRefPdONumber() {
        return this.refPdONumber;
    }
    
    /** Setter for property refPdONumber.
     * @param refPdONumber New value of property refPdONumber.
     *
     */
    public void setRefPdONumber(String refPdONumber) {
        this.refPdONumber = refPdONumber;
    }
    
    /** Getter for property refDfNumber.
     * @return Value of property refDfNumber.
     *
     */
    public String getRefDfNumber() {
        return this.refDfNumber;
    }
    
    /** Setter for property refDfNumber.
     * @param refDfNumber New value of property refDfNumber.
     *
     */
    public void setRefDfNumber(String refDfNumber) {
        this.refDfNumber = refDfNumber;
    }
    
    /** Getter for property listInventory.
     * @return Value of property listInventory.
     */
    public Vector getListInventory() {
        return listInventory;
    }
    
    /** Setter for property listInventory.
     * @param listInventory New value of property listInventory.
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
