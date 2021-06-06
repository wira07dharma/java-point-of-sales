/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class Voucher extends Entity {

    private String voucherNo = "";
    private String voucherName = "";
    private double voucherNominal = 0;
    private int voucherType = 0;
    private Date voucherCreateDate = new Date();
    private Date voucherIssuedDate = new Date();
    private Date voucherExpired = new Date();
    private long voucherOutlet = 0;
    private String voucherOutletName = "";
    private String voucherAuthorizedName = "";
    private long voucherAuthorizedId = 0;
    private int voucherStatus = 0;
    private String voucherIssuedTo = "";
    private String barcode ="";
    private long refCashBillMainId=0;
    
    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public double getVoucherNominal() {
        return voucherNominal;
    }

    public void setVoucherNominal(double voucherNominal) {
        this.voucherNominal = voucherNominal;
    }

    public int getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(int voucherType) {
        this.voucherType = voucherType;
    }

    public Date getVoucherCreateDate() {
        return voucherCreateDate;
    }

    public void setVoucherCreateDate(Date voucherCreateDate) {
        this.voucherCreateDate = voucherCreateDate;
    }

    public Date getVoucherIssuedDate() {
        return voucherIssuedDate;
    }

    public void setVoucherIssuedDate(Date voucherIssuedDate) {
        this.voucherIssuedDate = voucherIssuedDate;
    }

    public Date getVoucherExpired() {
        return voucherExpired;
    }

    public void setVoucherExpired(Date voucherExpired) {
        this.voucherExpired = voucherExpired;
    }

    public long getVoucherOutlet() {
        return voucherOutlet;
    }

    public void setVoucherOutlet(long voucherOutlet) {
        this.voucherOutlet = voucherOutlet;
    }

    public String getVoucherAuthorizedName() {
        return voucherAuthorizedName;
    }

    public void setVoucherAuthorizedName(String voucherAuthorizedName) {
        this.voucherAuthorizedName = voucherAuthorizedName;
    }

    public long getVoucherAuthorizedId() {
        return voucherAuthorizedId;
    }

    public void setVoucherAuthorizedId(long voucherAuthorizedId) {
        this.voucherAuthorizedId = voucherAuthorizedId;
    }

    public int getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(int voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public String getVoucherIssuedTo() {
        return voucherIssuedTo;
    }

    public void setVoucherIssuedTo(String voucherIssuedTo) {
        this.voucherIssuedTo = voucherIssuedTo;
    }

    /**
     * @return the voucherOutletName
     */
    public String getVoucherOutletName() {
        return voucherOutletName;
    }

    /**
     * @param voucherOutletName the voucherOutletName to set
     */
    public void setVoucherOutletName(String voucherOutletName) {
        this.voucherOutletName = voucherOutletName;
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the refCashBillMainId
     */
    public long getRefCashBillMainId() {
        return refCashBillMainId;
    }

    /**
     * @param refCashBillMainId the refCashBillMainId to set
     */
    public void setRefCashBillMainId(long refCashBillMainId) {
        this.refCashBillMainId = refCashBillMainId;
    }
}