/*
 * ArapDetailItem.java
 *
 * Created on June 9, 2006, 9:44 PM
 */

package com.dimata.interfaces.arap;

import java.util.Date;

/**
 *
 * @author  Administrator
 */
public class ArapDetailItem {
    
    /** Holds value of itemType */
    private int itemType = 0;
    
    /** Holds value of mainId */
    private long mainId = 0;
    
    /** Holds value of voucher number */
    private String voucherNo = "";
    
    /** Holds value of voucher date */
    private Date voucherDate = new Date();
    
    /** Holds value of due date */
    private Date dueDate = new Date();
    /** Holds value of nominal */
    private double nominal = 0;
    
    /** Holds value of payed */
    private double payed = 0;
    
    /** Holds value of description */
    private String description = "";

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public Date getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(Date voucherDate) {
        this.voucherDate = voucherDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getPayed() {
        return payed;
    }

    public void setPayed(double payed) {
        this.payed = payed;
    }

    public double getBalance() {
        return nominal-payed;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getMainId() {
        return mainId;
    }

    public void setMainId(long mainId) {
        this.mainId = mainId;
    }
    
}
