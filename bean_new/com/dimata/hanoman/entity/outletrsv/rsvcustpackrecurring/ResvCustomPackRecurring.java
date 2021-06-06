/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.rsvcustpackrecurring;

import com.dimata.qdep.entity.Entity;
import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author Dewa
 */
public class ResvCustomPackRecurring extends Entity {

    public static final int RECURRING_TYPE_DAILY = 1;
    public static final int RECURRING_TYPE_DATE = 2;
    public static final int RECURRING_TYPE_WEEK = 3;
    public static final int RECURRING_TYPE_MONTH = 4;
    public static final int RECURRING_TYPE_YEAR = 5;

    public static final String RECURRING_TYPE_TITLE[] = {"Daily", "By Date", "Weekly", "Monthly", "Yearly"};

    public static final int RECURRING_TYPE[] = {1, 2, 3, 4, 5};

    public static final int UNTIL_TYPE_DATE = 1;
    public static final int UNTIL_TYPE_REPETITIONS = 2;
    public static final int UNTIL_TYPE_FOREVER = 3;

    public static final String UNTIL_TITLE[] = {"Date", "Repetitions", "Forever"};

    public static final int UNTIL_TYPE[] = {1, 2, 3};

    private int repeatType = 0;
    private int repeatPeriode = 0;
    private int weekDays = 0;
    private int monthlyType = 0;
    private int dayOfMonth = 0;
    private int untilType = 0;
    private int repeatNumber = 0;
    private Date repeatUntilDate = null;
    private long customePackBillingId = 0;
    private int invoiceOption = 0;

    public int getInvoiceOption() {
        return invoiceOption;
    }

    public void setInvoiceOption(int invoiceOption) {
        this.invoiceOption = invoiceOption;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public int getRepeatPeriode() {
        return repeatPeriode;
    }

    public void setRepeatPeriode(int repeatPeriode) {
        this.repeatPeriode = repeatPeriode;
    }

    public int getWeekDays() {
        return weekDays;
    }

    public boolean getWeekDay(int Day) {
        return (this.weekDays & (int) Math.pow(2, Day - 1)) > 0;
    }

    public void setWeekDays(int weekDays) {
        this.weekDays = weekDays;
    }

    public void setWeekDay(int Day, boolean set) {

        if (set == true) {
            this.weekDays = this.weekDays | (int) Math.pow(2, Day - 1);
        } else {
            this.weekDays -= (int) Math.pow(2, Day - 1);
        }
    }

    public int getMonthlyType() {
        return monthlyType;
    }

    public void setMonthlyType(int monthlyType) {
        this.monthlyType = monthlyType;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getUntilType() {
        return untilType;
    }

    public void setUntilType(int untilType) {
        this.untilType = untilType;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    public Date getRepeatUntilDate() {
        return repeatUntilDate;
    }

    public void setRepeatUntilDate(Date repeatUntilDate) {
        this.repeatUntilDate = repeatUntilDate;
    }

    public long getCustomePackBillingId() {
        return customePackBillingId;
    }

    public void setCustomePackBillingId(long customePackBillingId) {
        this.customePackBillingId = customePackBillingId;
    }

}
