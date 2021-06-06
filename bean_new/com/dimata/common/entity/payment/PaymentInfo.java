/**
 * User: gwawan
 * Date: Mei 9, 2007
 * Time: 4:46:00 PM
 * Version: 1.0
 */
package com.dimata.common.entity.payment;

import com.dimata.qdep.entity.Entity;
import java.io.Serializable;
import java.util.Date;

public class PaymentInfo extends Entity implements Serializable {
    
    private String stBankName = "";
    private String stBankAddress = "";
    private String stSwiftCade = "";
    private String stAccountName = "";
    private String stAccountNumber = "";
    private String stNameOnCard = "";
    private String stCardNumber = "";
    private String stCardId = "";
    private Date dtExpiredDate;
    private String stCheckBGNumber = "";
    private double dBankCost;
    private long lPurchPaymentId;
    private String stPaymentAddress = "";
    private Date dueDate;
    private Date tanggalPencairan;
    
    public PaymentInfo() {
    }
    
    public String getPstClassName() {
        return this.getClass().getName();
    }
    
    public String getStBankName() {
        return stBankName;
    }
    
    public void setStBankName(String stBankName) {
        this.stBankName = stBankName;
    }
    
    public String getStBankAddress() {
        return stBankAddress;
    }
    
    public void setStBankAddress(String stBankAddress) {
        this.stBankAddress = stBankAddress;
    }
    
    public String getStSwiftCade() {
        return stSwiftCade;
    }
    
    public void setStSwiftCade(String stSwiftCade) {
        this.stSwiftCade = stSwiftCade;
    }
    
    public String getStAccountName() {
        return stAccountName;
    }
    
    public void setStAccountName(String stAccountName) {
        this.stAccountName = stAccountName;
    }
    
    public String getStAccountNumber() {
        return stAccountNumber;
    }
    
    public void setStAccountNumber(String stAccountNumber) {
        this.stAccountNumber = stAccountNumber;
    }
    
    public String getStNameOnCard() {
        return stNameOnCard;
    }
    
    public void setStNameOnCard(String stNameOnCard) {
        this.stNameOnCard = stNameOnCard;
    }
    
    public String getStCardNumber() {
        return stCardNumber;
    }
    
    public void setStCardNumber(String stCardNumber) {
        this.stCardNumber = stCardNumber;
    }
    
    public String getStCardId() {
        return stCardId;
    }
    
    public void setStCardId(String stCardId) {
        this.stCardId = stCardId;
    }
    
    public Date getDtExpiredDate() {
        return dtExpiredDate;
    }
    
    public void setDtExpiredDate(Date dtExpiredDate) {
        this.dtExpiredDate = dtExpiredDate;
    }
    
    public String getStCheckBGNumber() {
        return stCheckBGNumber;
    }
    
    public void setStCheckBGNumber(String stCheckBGNumber) {
        this.stCheckBGNumber = stCheckBGNumber;
    }
    
    public double getdBankCost() {
        return dBankCost;
    }
    
    public void setdBankCost(double dBankCost) {
        this.dBankCost = dBankCost;
    }
    
    public long getlPurchPaymentId() {
        return lPurchPaymentId;
    }
    
    public void setlPurchPaymentId(long lPurchPaymentId) {
        this.lPurchPaymentId = lPurchPaymentId;
    }
    
    public String getStPaymentAddress() {
        return stPaymentAddress;
    }
    
    public void setStPaymentAddress(String stPaymentAddress) {
        this.stPaymentAddress = stPaymentAddress;
    }
    
    public Date getDueDate() {
        return this.dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the tanggalPencairan
     */
    public Date getTanggalPencairan() {
        return tanggalPencairan;
    }

    /**
     * @param tanggalPencairan the tanggalPencairan to set
     */
    public void setTanggalPencairan(Date tanggalPencairan) {
        this.tanggalPencairan = tanggalPencairan;
    }
    
}
