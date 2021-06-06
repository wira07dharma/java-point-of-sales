/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class CashTellerBalance extends Entity {

    private long cashBalanceId = 0;
    private long tellerShiftId = 0;
    private long currencyId = 0;
    private int type = 0;
    private Date balanceDate = null;
    private double balanceValue = 0;
    private double shouldValue = 0;
    private long paymentSystemId = 0;

    public long getCashBalanceId() {
        return cashBalanceId;
    }

    public void setCashBalanceId(long cashBalanceId) {
        this.cashBalanceId = cashBalanceId;
    }

    public long getTellerShiftId() {
        return tellerShiftId;
    }

    public void setTellerShiftId(long tellerShiftId) {
        this.tellerShiftId = tellerShiftId;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public double getBalanceValue() {
        return balanceValue;
    }

    public void setBalanceValue(double balanceValue) {
        this.balanceValue = balanceValue;
    }

    public double getShouldValue() {
        return shouldValue;
    }

    public void setShouldValue(double shouldValue) {
        this.shouldValue = shouldValue;
    }

    public long getPaymentSystemId() {
        return paymentSystemId;
    }

    public void setPaymentSystemId(long paymentSystemId) {
        this.paymentSystemId = paymentSystemId;
    }

}
