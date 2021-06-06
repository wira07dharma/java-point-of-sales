/*
 * SrcInvoice.java
 *
 * Created on February 25, 2005, 5:57 PM
 */

package com.dimata.pos.entity.search;

import java.io.*;
import java.util.*;
/**
 *
 * @author  wpradnyana
 */
public class SrcInvoice implements Serializable
{

    /**
     * Holds value of property invoiceId.
     */
    private long invoiceId=0;

    /**
     * Holds value of property invoiceNumber.
     */
    private String invoiceNumber="";

    /**
     * Holds value of property transactionPoin.
     */
    private int transactionPoin=0;

    /**
     * Holds value of property memberId.
     */
    private long memberId=0;

    /**
     * Holds value of property memberName.
     */
    private String memberName="";

    /**
     * Holds value of property customerName.
     */
    private String customerName="";

    /**
     * Holds value of property invoiceDate.
     */
    private java.util.Date invoiceDate = new java.util.Date();

    /**
     * Holds value of property invoiceDateTo.
     */
    private java.util.Date invoiceDateTo = new java.util.Date();

    /**
     * Holds value of property transStatus.
     */
    private int transStatus;

    /**
     * Ari wiweka 20130710
     * untuk srcsalesorder
     */
    private String salesPerson = "";
    private String SalesName = "";
    private long materialId = 0;
    private String matName = "";
    private String sku = "";
    private long currId = 0;
    private long salesId = 0;
    private int statusDate = 0;
    private Vector prmstatus = new Vector();
    private Vector prmstatusCurr = new Vector();
    private String prmnumber = "";
    private int docType = 0;
    private int transType = 0;
    private int sortBy = 0;
    private String salesCode="";
    /** Creates a new instance of SrcInvoice */
    // update by mchen | 2014-12-09
    private int statusDraff = 0;
    private int statusOnProcess = 0;
    private int statusDone = 0;
    public SrcInvoice ()
    {
    }

    /**
     * Getter for property invoiceId.
     * @return Value of property invoiceId.
     */
    public long getInvoiceId ()
    {
        return this.invoiceId;
    }

    /**
     * Setter for property invoiceId.
     * @param invoiceId New value of property invoiceId.
     */
    public void setInvoiceId (long invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    /**
     * Getter for property invoiceNumber.
     * @return Value of property invoiceNumber.
     */
    public String getInvoiceNumber ()
    {
        return this.invoiceNumber;
    }

    /**
     * Setter for property invoiceNumber.
     * @param invoiceNumber New value of property invoiceNumber.
     */
    public void setInvoiceNumber (String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    /**
     * Getter for property transactionPoin.
     * @return Value of property transactionPoin.
     */
    public int getTransactionPoin ()
    {
        return this.transactionPoin;
    }

    /**
     * Setter for property transactionPoin.
     * @param transactionPoin New value of property transactionPoin.
     */
    public void setTransactionPoin (int transactionPoin)
    {
        this.transactionPoin = transactionPoin;
    }

    /**
     * Getter for property memberId.
     * @return Value of property memberId.
     */
    public long getMemberId ()
    {
        return this.memberId;
    }

    /**
     * Setter for property memberId.
     * @param memberId New value of property memberId.
     */
    public void setMemberId (long memberId)
    {
        this.memberId = memberId;
    }

    /**
     * Getter for property memberName.
     * @return Value of property memberName.
     */
    public String getMemberName ()
    {
        return this.memberName;
    }

    /**
     * Setter for property memberName.
     * @param memberName New value of property memberName.
     */
    public void setMemberName (String memberName)
    {
        this.memberName = memberName;
    }

    /**
     * Getter for property customerName.
     * @return Value of property customerName.
     */
    public String getCustomerName ()
    {
        return this.customerName;
    }

    /**
     * Setter for property customerName.
     * @param customerName New value of property customerName.
     */
    public void setCustomerName (String customerName)
    {
        this.customerName = customerName;
    }

    /**
     * Getter for property invoiceDate.
     * @return Value of property invoiceDate.
     */
    public java.util.Date getInvoiceDate ()
    {
        return this.invoiceDate;
    }

    /**
     * Setter for property invoiceDate.
     * @param invoiceDate New value of property invoiceDate.
     */
    public void setInvoiceDate (java.util.Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    /**
     * Getter for property invoiceDateTo.
     * @return Value of property invoiceDateTo.
     */
    public java.util.Date getInvoiceDateTo ()
    {
        return this.invoiceDateTo;
    }

    /**
     * Setter for property invoiceDateTo.
     * @param invoiceDateTo New value of property invoiceDateTo.
     */
    public void setInvoiceDateTo (java.util.Date invoiceDateTo)
    {
        this.invoiceDateTo = invoiceDateTo;
    }

    public String printValues(){
        StringBuffer buff = new StringBuffer();
        try{
            buff.append ("\n customername "+getCustomerName ());
            //buff.append ("\n invioceDate "+getInvoiceDate ().toString ());
            //buff.append ("\n invoiceDateTo "+getInvoiceDateTo ().toString ());
            buff.append ("\n invoiceNumber "+getInvoiceId ());
            buff.append ("\n memberId "+getMemberId ());
            buff.append ("\n memberName "+getMemberName ());
            buff.append ("\n transactionPoin "+getTransactionPoin ());
        }catch(Exception e){
            e.printStackTrace ();
        }
        String result = new String(buff);
        return result;
    }

    /**
     * Getter for property transStatus.
     * @return Value of property transStatus.
     */
    public int getTransStatus ()
    {
        return this.transStatus;
    }

    /**
     * Setter for property transStatus.
     * @param transStatus New value of property transStatus.
     */
    public void setTransStatus (int transStatus)
    {
        this.transStatus = transStatus;
    }

    /**
     * @return the salesPerson
     */
    public String getSalesPerson() {
        return salesPerson;
    }

    /**
     * @param salesPerson the salesPerson to set
     */
    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    /**
     * @return the SalesName
     */
    public String getSalesName() {
        return SalesName;
    }

    /**
     * @param SalesName the SalesName to set
     */
    public void setSalesName(String SalesName) {
        this.SalesName = SalesName;
    }

    /**
     * @return the materialId
     */
    public long getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    /**
     * @return the matName
     */
    public String getMatName() {
        return matName;
    }

    /**
     * @param matName the matName to set
     */
    public void setMatName(String matName) {
        this.matName = matName;
    }

    /**
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * @return the currId
     */
    public long getCurrId() {
        return currId;
    }

    /**
     * @param currId the currId to set
     */
    public void setCurrId(long currId) {
        this.currId = currId;
    }

    /**
     * @return the salesId
     */
    public long getSalesId() {
        return salesId;
    }

    /**
     * @param salesId the salesId to set
     */
    public void setSalesId(long salesId) {
        this.salesId = salesId;
    }

    /**
     * @return the statusDate
     */
    public int getStatusDate() {
        return statusDate;
    }

    /**
     * @param statusDate the statusDate to set
     */
    public void setStatusDate(int statusDate) {
        this.statusDate = statusDate;
    }

    /**
     * @return the prmstatus
     */
    public Vector getPrmstatus() {
        return prmstatus;
    }

    /**
     * @param prmstatus the prmstatus to set
     */
    public void setPrmstatus(Vector prmstatus) {
        this.prmstatus = prmstatus;
    }


    public String getPrmnumber(){ return prmnumber; }

    public void setPrmnumber(String prmnumber)
    {
        if ( prmnumber == null )
        {
            prmnumber = "";
	}
	this.prmnumber = prmnumber;
    }

    /**
     * @return the docType
     */
    public int getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(int docType) {
        this.docType = docType;
    }

    /**
     * @return the transType
     */
    public int getTransType() {
        return transType;
    }

    /**
     * @param transType the transType to set
     */
    public void setTransType(int transType) {
        this.transType = transType;
    }

    /**
     * @return the prmstatusCurr
     */
    public Vector getPrmstatusCurr() {
        return prmstatusCurr;
}

    /**
     * @param prmstatusCurr the prmstatusCurr to set
     */
    public void setPrmstatusCurr(Vector prmstatusCurr) {
        this.prmstatusCurr = prmstatusCurr;
    }

    /**
     * @return the sortBy
     */
    public int getSortBy() {
        return sortBy;
    }

    /**
     * @param sortBy the sortBy to set
     */
    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * @return the salesCode
     */
    public String getSalesCode() {
        return salesCode;
    }

    /**
     * @param salesCode the salesCode to set
     */
    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    /**
     * @return the statusDraff
     */
    public int getStatusDraff() {
        return statusDraff;
}

    /**
     * @param statusDraff the statusDraff to set
     */
    public void setStatusDraff(int statusDraff) {
        this.statusDraff = statusDraff;
    }

    /**
     * @return the statusOnProcess
     */
    public int getStatusOnProcess() {
        return statusOnProcess;
    }

    /**
     * @param statusOnProcess the statusOnProcess to set
     */
    public void setStatusOnProcess(int statusOnProcess) {
        this.statusOnProcess = statusOnProcess;
    }

    /**
     * @return the statusDone
     */
    public int getStatusDone() {
        return statusDone;
    }

    /**
     * @param statusDone the statusDone to set
     */
    public void setStatusDone(int statusDone) {
        this.statusDone = statusDone;
    }
}
