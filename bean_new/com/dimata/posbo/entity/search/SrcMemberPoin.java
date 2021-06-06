/*
 * SrcMemberPoin.java
 *
 * Created on February 25, 2005, 4:17 PM
 */

package com.dimata.posbo.entity.search;

import java.io.*;
/**
 *
 *
 * @author  wpradnyana
 */
public class SrcMemberPoin implements Serializable
{
    
    public static final int GROUP_BY_MEMBER=0;
    public static final int GROUP_BY_ITEM=1;
    
    public static final int SORT_BY_MEMBER_NAME=0;
    public static final int SORT_BY_MEMBER_CODE=1;
    public static final int SORT_BY_TOTAL_POIN=2;
    public static final int SORT_BY_TOTAL_FREE=3; 
    
    public static final int SORT_ASC=0;
    public static final int SORT_DESC=1; 
    /**
     * Holds value of property memberCode.
     */
    private String memberCode; 
    
    /**
     * Holds value of property memberName.
     */
    private String memberName;
    
    /**
     * Holds value of property poinFrom.
     */
    private int poinFrom;
    
    /**
     * Holds value of property poinTo.
     */
    private int poinTo;
    
    /**
     * Holds value of property memberId.
     */
    private long memberId;
    
    /**
     * Holds value of property poinId.
     */
    private long poinId;
    
    /**
     * Holds value of property invoiceId.
     */
    private long invoiceId;
    
    /**
     * Holds value of property invoiceNumber.
     */
    private String invoiceNumber;
    
    /**
     * Holds value of property groupBy.
     */
    private int groupBy;
    
    /**
     * Holds value of property sortBy.
     */
    private int sortBy;
    
    /**
     * Holds value of property sortMethod.
     */
    private int sortMethod;
    
    /**
     * Holds value of property usePoinRange.
     */
    private boolean usePoinRange=true;
    
    /** Creates a new instance of SrcMemberPoin */
    public SrcMemberPoin ()
    {
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
     * Getter for property poinFrom.
     * @return Value of property poinFrom.
     */
    public int getPoinFrom ()
    {
        return this.poinFrom;
    }
    
    /**
     * Setter for property poinFrom.
     * @param poinFrom New value of property poinFrom.
     */
    public void setPoinFrom (int poinFrom)
    {
        this.poinFrom = poinFrom;
    }
    
    /**
     * Getter for property poinTo.
     * @return Value of property poinTo.
     */
    public int getPoinTo ()
    {
        return this.poinTo;
    }
    
    /**
     * Setter for property poinTo.
     * @param poinTo New value of property poinTo.
     */
    public void setPoinTo (int poinTo)
    {
        this.poinTo = poinTo;
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
     * Getter for property groupBy.
     * @return Value of property groupBy.
     */
    public int getGroupBy ()
    {
        return this.groupBy;
    }
    
    /**
     * Setter for property groupBy.
     * @param groupBy New value of property groupBy.
     */
    public void setGroupBy (int groupBy)
    {
        this.groupBy = groupBy;
    }
    
    /**
     * Getter for property sortBy.
     * @return Value of property sortBy.
     */
    public int getSortBy ()
    {
        return this.sortBy;
    }
    
    /**
     * Setter for property sortBy.
     * @param sortBy New value of property sortBy.
     */
    public void setSortBy (int sortBy)
    {
        this.sortBy = sortBy;
    }
    
    /**
     * Getter for property sortMethod.
     * @return Value of property sortMethod.
     */
    public int getSortMethod ()
    {
        return this.sortMethod;
    }
    
    /**
     * Setter for property sortMethod.
     * @param sortMethod New value of property sortMethod.
     */
    public void setSortMethod (int sortMethod)
    {
        this.sortMethod = sortMethod;
    }
    
    /**
     * Getter for property usePoinRange.
     * @return Value of property usePoinRange.
     */
    public boolean isUsePoinRange ()
    {
        return this.usePoinRange;
    }
    
    /**
     * Setter for property usePoinRange.
     * @param usePoinRange New value of property usePoinRange.
     */
    public void setUsePoinRange (boolean usePoinRange)
    {
        this.usePoinRange = usePoinRange;
    }
    
}
