/*
 * MemberPoinItem.java
 *
 * Created on February 25, 2005, 4:59 PM
 */

package com.dimata.posbo.session.memberpoin;

import java.io.*;
/**
 *
 * @author  wpradnyana
 */
public class MemberPoinItem implements Serializable
{
    
    /**
     * Holds value of property invoiceId.
     */
    private long invoiceId;
    
    /**
     * Holds value of property memberId.
     */
    private long memberId;
    
    /**
     * Holds value of property memberName.
     */
    private String memberName;
    
    /**
     * Holds value of property memberCode.
     */
    private String memberCode;
    
    /**
     * Holds value of property totalPoin.
     */
    private int totalPoin;
    
    /**
     * Holds value of property totalPoinUsed.
     */
    private int totalPoinUsed;
    
    /**
     * Holds value of property totalPoinFree.
     */
    private int totalPoinFree;
    
    /**
     * Holds value of property invoiceNumber.
     */
    private String invoiceNumber;
    
    /**
     * Holds value of property invoiceDate.
     */
    private java.util.Date invoiceDate;
    
    /**
     * Holds value of property poinId.
     */
    private long poinId;
    
    /**
     * Holds value of property transactionDate.
     */
    private java.util.Date transactionDate;
    
    /** Creates a new instance of MemberPoinItem */
    public MemberPoinItem ()
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
     * Getter for property memberCode.
     * @return Value of property memberCode.
     */
    public String getMemberCode ()
    {
        return this.memberCode;
    }
    
    /**
     * Setter for property memberCode.
     * @param memberCode New value of property memberCode.
     */
    public void setMemberCode (String memberCode)
    {
        this.memberCode = memberCode;
    }
    
    /**
     * Getter for property totalPoin.
     * @return Value of property totalPoin.
     */
    public int getTotalPoin ()
    {
        return this.totalPoin;
    }
    
    /**
     * Setter for property totalPoin.
     * @param totalPoin New value of property totalPoin.
     */
    public void setTotalPoin (int totalPoin)
    {
        this.totalPoin = totalPoin;
    }
    
    /**
     * Getter for property totalPoinUsed.
     * @return Value of property totalPoinUsed.
     */
    public int getTotalPoinUsed ()
    {
        return this.totalPoinUsed;
    }
    
    /**
     * Setter for property totalPoinUsed.
     * @param totalPoinUsed New value of property totalPoinUsed.
     */
    public void setTotalPoinUsed (int totalPoinUsed)
    {
        this.totalPoinUsed = totalPoinUsed;
    }
    
    /**
     * Getter for property totalPoinFree.
     * @return Value of property totalPoinFree.
     */
    public int getTotalPoinFree ()
    {
        return this.totalPoinFree;
    }
    
    /**
     * Setter for property totalPoinFree.
     * @param totalPoinFree New value of property totalPoinFree.
     */
    public void setTotalPoinFree (int totalPoinFree)
    {
        this.totalPoinFree = totalPoinFree;
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
    
    public String printValue(){
        StringBuffer buff = new StringBuffer();
        buff.append ("\n invoice id "+this.getInvoiceId ());
        buff.append ("\n invoice number "+this.getInvoiceNumber ());
        buff.append ("\n member code "+this.getMemberCode ());
        buff.append ("\n member id "+this.getMemberId ());
        buff.append ("\n member name "+this.getMemberName ());
        buff.append ("\n total poin "+this.getTotalPoin ());
        buff.append ("\n total poin free "+this.getTotalPoinFree ());
        buff.append ("\n total poin used "+this.getTotalPoinUsed ());
        buff.append ("\n poin id "+this.getPoinId());
        String result = new String(buff);
        return result;
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
     * Getter for property poinId.
     * @return Value of property poinId.
     */
    public long getPoinId ()
    {
        return this.poinId;
    }
    
    /**
     * Setter for property poinId.
     * @param poinId New value of property poinId.
     */
    public void setPoinId (long poinId)
    {
        this.poinId = poinId;
    }
    
    /**
     * Getter for property transactionDate.
     * @return Value of property transactionDate.
     */
    public java.util.Date getTransactionDate ()
    {
        return this.transactionDate;
    }
    
    /**
     * Setter for property transactionDate.
     * @param transactionDate New value of property transactionDate.
     */
    public void setTransactionDate (java.util.Date transactionDate)
    {
        this.transactionDate = transactionDate;
    }
    
}
