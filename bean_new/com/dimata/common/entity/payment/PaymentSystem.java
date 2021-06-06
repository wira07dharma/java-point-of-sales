
/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 * *****************************************************************
 */
package com.dimata.common.entity.payment;

/* package java */
import java.util.Date;
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class PaymentSystem extends Entity implements Serializable {

    /**
     * Holds value of property paymentSystem.
     */
    private String paymentSystem = "";

    /**
     * Holds value of property description.
     */
    private String description = "";

    /**
     * Holds value of property bankInfoOut.
     */
    private boolean bankInfoOut = false;

    /**
     * Holds value of property cardInfo.
     */
    private boolean cardInfo = false;

    /**
     * Holds value of property dueDateInfo.
     */
    private boolean dueDateInfo = false;

    /**
     * Holds value of property days.
     */
    private int days;

    /**
     * Holds value of property checkBGInfo.
     */
    private boolean checkBGInfo = false;

    /**
     * Holds value of property bankInfoIn.
     */
    private boolean bankInfoIn = false;

    /**
     * Holds value of property bankName.
     */
    private String bankName = "";

    /**
     * Holds value of property bankAddress.
     */
    private String bankAddress = "";

    /**
     * Holds value of property swiftCode.
     */
    private String swiftCode = "";

    /**
     * Holds value of property accountName.
     */
    private String accountName = "";

    /**
     * Holds value of property accountNumber.
     */
    private String accountNumber = "";

    /**
     * Holds value of property infoStatus.
     */
    private int infoStatus = 0;

    private boolean fixed = false;

    private long clearedRefId = 0;

    private int paymentType;

    private double chargeToCustomerPercent = 0;

    private double bankCostPercent = 0;

    private long costingTo = 0;

    //added by dewok 20190309 for payment type return
    private int enableForReturn = 0;

    public String getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(String paymentSystem) {
        if (paymentSystem == null) {
            paymentSystem = "";
        }
        this.paymentSystem = paymentSystem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            description = "";
        }
        this.description = description;
    }

    /**
     * Getter for property bankInfoOut.
     *
     * @return Value of property bankInfoOut.
     *
     */
    public boolean isBankInfoOut() {
        return this.bankInfoOut;
    }

    /**
     * Setter for property bankInfoOut.
     *
     * @param bankInfoOut New value of property bankInfoOut.
     *
     */
    public void setBankInfoOut(boolean bankInfoOut) {
        this.bankInfoOut = bankInfoOut;
    }

    /**
     * Getter for property cardInfo.
     *
     * @return Value of property cardInfo.
     *
     */
    public boolean isCardInfo() {
        return this.cardInfo;
    }

    /**
     * Setter for property cardInfo.
     *
     * @param cardInfo New value of property cardInfo.
     *
     */
    public void setCardInfo(boolean cardInfo) {
        this.cardInfo = cardInfo;
    }

    /**
     * Getter for property dueDate.
     *
     * @return Value of property dueDate.
     *
     */
    public boolean isDueDateInfo() {
        return this.dueDateInfo;
    }

    /**
     * Setter for property dueDate.
     *
     * @param dueDate New value of property dueDate.
     *
     */
    public void setDueDateInfo(boolean dueDateInfo) {
        this.dueDateInfo = dueDateInfo;
    }

    /**
     * Getter for property days.
     *
     * @return Value of property days.
     *
     */
    public int getDays() {
        return this.days;
    }

    /**
     * Setter for property days.
     *
     * @param days New value of property days.
     *
     */
    public void setDays(int days) {
        this.days = days;
    }

    /**
     * Getter for property checkBGInfo.
     *
     * @return Value of property checkBGInfo.
     *
     */
    public boolean isCheckBGInfo() {
        return this.checkBGInfo;
    }

    /**
     * Setter for property checkBGInfo.
     *
     * @param checkBGInfo New value of property checkBGInfo.
     *
     */
    public void setCheckBGInfo(boolean checkBGInfo) {
        this.checkBGInfo = checkBGInfo;
    }

    /**
     * Getter for property bankInfoIn.
     *
     * @return Value of property bankInfoIn.
     *
     */
    public boolean isBankInfoIn() {
        return this.bankInfoIn;
    }

    /**
     * Setter for property bankInfoIn.
     *
     * @param bankInfoIn New value of property bankInfoIn.
     *
     */
    public void setBankInfoIn(boolean bankInfoIn) {
        this.bankInfoIn = bankInfoIn;
    }

    /**
     * Getter for property bankName.
     *
     * @return Value of property bankName.
     *
     */
    public String getBankName() {
        return this.bankName;
    }

    /**
     * Setter for property bankName.
     *
     * @param bankName New value of property bankName.
     *
     */
    public void setBankName(String bankName) {
        if (bankName == null) {
            bankName = "";
        }

        this.bankName = bankName;
    }

    /**
     * Getter for property bankAddress.
     *
     * @return Value of property bankAddress.
     *
     */
    public String getBankAddress() {
        return this.bankAddress;
    }

    /**
     * Setter for property bankAddress.
     *
     * @param bankAddress New value of property bankAddress.
     *
     */
    public void setBankAddress(String bankAddress) {
        if (bankAddress == null) {
            bankAddress = "";
        }

        this.bankAddress = bankAddress;
    }

    /**
     * Getter for property swiftCode.
     *
     * @return Value of property swiftCode.
     *
     */
    public String getSwiftCode() {
        return this.swiftCode;
    }

    /**
     * Setter for property swiftCode.
     *
     * @param swiftCode New value of property swiftCode.
     *
     */
    public void setSwiftCode(String swiftCode) {
        if (swiftCode == null) {
            swiftCode = "";
        }
        this.swiftCode = swiftCode;
    }

    /**
     * Getter for property accountName.
     *
     * @return Value of property accountName.
     *
     */
    public String getAccountName() {
        return this.accountName;
    }

    /**
     * Setter for property account
     *
     * @param accountName New value of property accountName.
     *
     */
    public void setAccountName(String accountName) {
        if (accountName == null) {
            accountName = "";
        }
        this.accountName = accountName;
    }

    /**
     * Getter for property accountNumber.
     *
     * @return Value of property accountNumber.
     *
     */
    public String getAccountNumber() {

        return this.accountNumber;
    }

    /**
     * Setter for property accountNumber.
     *
     * @param accountNumber New value of property accountNumber.
     *
     */
    public void setAccountNumber(String accountNumber) {
        if (accountNumber == null) {
            accountNumber = "";
        }
        this.accountNumber = accountNumber;
    }

    /**
     * Getter for property infoStatus.
     *
     * @return Value of property infoStatus.
     *
     */
    public int getInfoStatus() {
        return this.infoStatus;
    }

    /**
     * Setter for property infoStatus.
     *
     * @param infoStatus New value of property infoStatus.
     *
     */
    public void setInfoStatus(int infoStatus) {
        this.infoStatus = infoStatus;
    }

    /**
     * Getter for property clearedRefId.
     *
     * @return Value of property clearedRefId.
     *
     */
    public long getClearedRefId() {
        return clearedRefId;
    }

    /**
     * Setter for property clearedRefId.
     *
     * @param clearedRefId New value of property clearedRefId.
     *
     */
    public void setClearedRefId(long clearedRefId) {
        this.clearedRefId = clearedRefId;
    }

    /**
     * Getter for property fixed.
     *
     * @return Value of property fixed.
     *
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * Setter for property fixed.
     *
     * @param fixed New value of property fixed.
     *
     */
    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public String getPstClassName() {
        return "com.dimata.common.entity.payment.PstPaymentSystem";
    }

    /**
     * Getter for property paymentType.
     *
     * @return Value of property paymentType.
     */
    public int getPaymentType() {
        return paymentType;
    }

    /**
     * Setter for property paymentType.
     *
     * @param paymentType New value of property paymentType.
     */
    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the chargeToCustomerPercent
     */
    public double getChargeToCustomerPercent() {
        return chargeToCustomerPercent;
    }

    /**
     * @param chargeToCustomerPercent the chargeToCustomerPercent to set
     */
    public void setChargeToCustomerPercent(double chargeToCustomerPercent) {
        this.chargeToCustomerPercent = chargeToCustomerPercent;
    }

    /**
     * @return the bankCostPercent
     */
    public double getBankCostPercent() {
        return bankCostPercent;
    }

    /**
     * @param bankCostPercent the bankCostPercent to set
     */
    public void setBankCostPercent(double bankCostPercent) {
        this.bankCostPercent = bankCostPercent;
    }

    /**
     * @return the costingTo
     */
    public long getCostingTo() {
        return costingTo;
    }

    /**
     * @param costingTo the costingTo to set
     */
    public void setCostingTo(long costingTo) {
        this.costingTo = costingTo;
    }

    public int getEnableForReturn() {
        return enableForReturn;
    }

    public void setEnableForReturn(int enableForReturn) {
        this.enableForReturn = enableForReturn;
    }

}
