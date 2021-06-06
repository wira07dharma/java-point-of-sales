package com.dimata.aiso.entity.search;

import java.util.*;

public class SrcJurnalUmum {

    public static final String SESS_SEARCH_JURNAL_UMUM = "SESS_SEARCH_JURNAL_UMUM";
    

    private long periodeId = 0;
    private int dateStatus = 0;
    private Date startDate;
    private Date endDate;
    private String invoice = "";
    private long operatorId = 0;
    private String voucher = "";
    private int currency;
    private int orderBy = 0;
    private long contactOid = 0;

    /**
     * Holds value of property voucherNo.
     */
    private String voucherNo = "";
    
    /**
     * Holds value of property strJournalNumber.
     */
    private String strJournalNumber = "";
    
    public long getContactOid() {
        return contactOid;
    }

    public void setContactOid(long contactOid) {
        this.contactOid = contactOid;
    }

    public long getPeriodeId() {
        return periodeId;
    }

    public void setPeriodeId(long periodeId) {
        this.periodeId = periodeId;
    }

    public int getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(int dateStatus) {
        this.dateStatus = dateStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }
    
    /**
     * Getter for property voucherNo.
     * @return Value of property voucherNo.
     */
    public String getVoucherNo() {
        return this.voucherNo;
    }
    
    /**
     * Setter for property voucherNo.
     * @param voucherNo New value of property voucherNo.
     */
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    
    /**
     * Getter for property strJournalNumber.
     * @return Value of property strJournalNumber.
     */
    public String getStrJournalNumber() {
        return this.strJournalNumber;
    }
    
    /**
     * Setter for property strJournalNumber.
     * @param strJournalNumber New value of property strJournalNumber.
     */
    public void setStrJournalNumber(String strJournalNumber) {
        this.strJournalNumber = strJournalNumber;
    }
    
}
