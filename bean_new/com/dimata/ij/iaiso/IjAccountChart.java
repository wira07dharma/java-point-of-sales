/*
 * AccountChart.java
 *
 * Created on December 29, 2004, 12:00 PM
 */

package com.dimata.ij.iaiso;


/**
 *
 * @author  Administrator
 * @version 
 */
public class IjAccountChart {

    /** Holds value of property accOid. */
    private long accOid = 0;
    
    /** Holds value of property accNumber. */
    private String accNumber = "";
    
    /** Holds value of property accName. */
    private String accName = "";
    
    /** Holds value of property accParent. */
    private long accParent = 0;
    
    /** Holds value of property accLevel. */
    private int accLevel = 0;
    
    /** Holds value of property accNormalSign. */
    private int accNormalSign = 0;
    
    /** Holds value of property accStatus. */
    private int accStatus;
    
    /** Holds value of property accGroup. */
    private int accGroup;
    
    /** Creates new AccountChart */
    public IjAccountChart() {
    }

    /** Getter for property accOid.
     * @return Value of property accOid.
     *
     */
    public long getAccOid() {
        return this.accOid;
    }
    
    /** Setter for property accOid.
     * @param accOid New value of property accOid.
     *
     */
    public void setAccOid(long accOid) {
        this.accOid = accOid;
    }
    
    /** Getter for property accNumber.
     * @return Value of property accNumber.
     *
     */
    public String getAccNumber() {
        return this.accNumber;
    }
    
    /** Setter for property accNumber.
     * @param accNumber New value of property accNumber.
     *
     */
    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }
    
    /** Getter for property accName.
     * @return Value of property accName.
     *
     */
    public String getAccName() {
        return this.accName;
    }
    
    /** Setter for property accName.
     * @param accName New value of property accName.
     *
     */
    public void setAccName(String accName) {
        this.accName = accName;
    }
    
    /** Getter for property accParent.
     * @return Value of property accParent.
     *
     */
    public long getAccParent() {
        return this.accParent;
    }
    
    /** Setter for property accParent.
     * @param accParent New value of property accParent.
     *
     */
    public void setAccParent(long accParent) {
        this.accParent = accParent;
    }
    
    /** Getter for property accLevel.
     * @return Value of property accLevel.
     *
     */
    public int getAccLevel() {
        return this.accLevel;
    }
    
    /** Setter for property accLevel.
     * @param accLevel New value of property accLevel.
     *
     */
    public void setAccLevel(int accLevel) {
        this.accLevel = accLevel;
    }
    
    /** Getter for property accNormalSide.
     * @return Value of property accNormalSide.
     *
     */
    public int getAccNormalSign() {
        return this.accNormalSign;
    }
    
    /** Setter for property accNormalSide.
     * @param accNormalSide New value of property accNormalSide.
     *
     */
    public void setAccNormalSign(int accNormalSign) {
        this.accNormalSign = accNormalSign;
    }
    
    /** Getter for property accStatus.
     * @return Value of property accStatus.
     *
     */
    public int getAccStatus() {
        return this.accStatus;
    }
    
    /** Setter for property accStatus.
     * @param accStatus New value of property accStatus.
     *
     */
    public void setAccStatus(int accStatus) {
        this.accStatus = accStatus;
    }
    
    /** Getter for property accGroup.
     * @return Value of property accGroup.
     *
     */
    public int getAccGroup() {
        return this.accGroup;
    }
    
    /** Setter for property accGroup.
     * @param accGroup New value of property accGroup.
     *
     */
    public void setAccGroup(int accGroup) {
        this.accGroup = accGroup;
    }
    
}
