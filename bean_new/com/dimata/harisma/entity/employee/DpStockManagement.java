/**
 * User: wardana
 * Date: Apr 8, 2004
 * Time: 12:32:33 PM
 * Version: 1.0 
 */
package com.dimata.harisma.entity.employee;

import com.dimata.qdep.entity.Entity;

import java.util.Date;

public class DpStockManagement extends Entity{

    private long lLeaveStockId;
    private int iDpQty;
    private Date dtOwningDate;
    private Date dtExpiredDate;
    private int iExceptionFlag;
    private Date dtExpiredDateExc;
    private int iDpStatus;
    private String stNote = "";

    public DpStockManagement() {
    }

    public long getlLeaveStockId() {
        return lLeaveStockId;
    }

    public void setlLeaveStockId(long lLeaveStockId) {
        this.lLeaveStockId = lLeaveStockId;
    }

    public int getiDpQty() {
        return iDpQty;
    }

    public void setiDpQty(int iDpQty) {
        this.iDpQty = iDpQty;
    }

    public Date getDtOwningDate() {
        return dtOwningDate;
    }

    public void setDtOwningDate(Date dtOwningDate) {
        this.dtOwningDate = dtOwningDate;
    }

    public Date getDtExpiredDate() {
        return dtExpiredDate;
    }

    public void setDtExpiredDate(Date dtExpiredDate) {
        this.dtExpiredDate = dtExpiredDate;
    }

    public int getiExceptionFlag() {
        return iExceptionFlag;
    }

    public void setiExceptionFlag(int iExceptionFlag) {
        this.iExceptionFlag = iExceptionFlag;
    }

    public Date getDtExpiredDateExc() {
        return dtExpiredDateExc;
    }

    public void setDtExpiredDateExc(Date dtExpiredDateExc) {
        this.dtExpiredDateExc = dtExpiredDateExc;
    }

    public int getiDpStatus() {
        return iDpStatus;
    }

    public void setiDpStatus(int iDpStatus) {
        this.iDpStatus = iDpStatus;
    }

    public String getStNote() {
        return stNote;
    }

    public void setStNote(String stNote) {
        this.stNote = stNote;
    }
}
