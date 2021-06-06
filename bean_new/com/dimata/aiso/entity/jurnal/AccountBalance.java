/*
 * AccountBalance.java
 *
 * Created on August 25, 2005, 11:03 AM
 */

package com.dimata.aiso.entity.jurnal;

// import qdep 
import com.dimata.qdep.entity.*; 

/**
 *
 * @author  gedhy
 */
public class AccountBalance extends Entity {
    
    /** Holds value of property lPerkiraanOid. */
    private long lPerkiraanOid;
        
    /** Holds value of property dDebet. */
    private double dDebet;
    
    /** Holds value of property dCredit. */
    private double dCredit;
    
    /** Holds value of property iNormalSign. */
    private int iNormalSign;
    
    /** Creates a new instance of AccountBalance */
    public AccountBalance() {
    }
    
    /** Getter for property lPerkiraanOid.
     * @return Value of property lPerkiraanOid.
     *
     */
    public long getLPerkiraanOid() {
        return this.lPerkiraanOid;
    }
    
    /** Setter for property lPerkiraanOid.
     * @param lPerkiraanOid New value of property lPerkiraanOid.
     *
     */
    public void setLPerkiraanOid(long lPerkiraanOid) {
        this.lPerkiraanOid = lPerkiraanOid;
    }
    
    /** Getter for property dDebet.
     * @return Value of property dDebet.
     *
     */
    public double getDDebet() {
        return this.dDebet;
    }
    
    /** Setter for property dDebet.
     * @param dDebet New value of property dDebet.
     *
     */
    public void setDDebet(double dDebet) {
        this.dDebet = dDebet;
    }
    
    /** Getter for property dCredit.
     * @return Value of property dCredit.
     *
     */
    public double getDCredit() {
        return this.dCredit;
    }
    
    /** Setter for property dCredit.
     * @param dCredit New value of property dCredit.
     *
     */
    public void setDCredit(double dCredit) {
        this.dCredit = dCredit;
    }
    
    /** Getter for property iNormalSign.
     * @return Value of property iNormalSign.
     *
     */
    public int getINormalSign() {
        return this.iNormalSign;
    }
    
    /** Setter for property iNormalSign.
     * @param iNormalSign New value of property iNormalSign.
     *
     */
    public void setINormalSign(int iNormalSign) {
        this.iNormalSign = iNormalSign;
    }
    
}
