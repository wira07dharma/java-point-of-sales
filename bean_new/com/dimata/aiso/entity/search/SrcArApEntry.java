package com.dimata.aiso.entity.search;

import java.util.Date;

/**
 * User: pulantara
 * Date: Oct 12, 2005
 * Time: 5:07:11 PM
 * Description:
 */
public class SrcArApEntry {

    private String voucherNo = "";
    private String notaNo = "";
    private String contactName = "";
    private Date fromDate = new Date();
    private Date untilDate = new Date();
    private int orderType = 0;
    private int type = 0;
    private int arApType = 0;

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getNotaNo() {
        return notaNo;
    }

    public void setNotaNo(String notaNo) {
        this.notaNo = notaNo;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getArApType() {
        return arApType;
    }

    public void setArApType(int arApType) {
        this.arApType = arApType;
    }
}
