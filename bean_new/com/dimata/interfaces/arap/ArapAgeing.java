/*
 * ArapAgeing.java
 *
 * Created on June 3, 2006, 11:43 PM
 */

package com.dimata.interfaces.arap;

/**
 *
 * @author  Administrator
 */
public class ArapAgeing {
    
    /** Holds value of  contactId */
    private long contactId = 0;
    
    /** Holds value of contactName */
    private String contactName = "";
    
    /** Holds value of todayDueDateValue */
    private double todayDueDateValue = 0;
    
    /** Holds value of tomorrowDueDateValue */
    private double tomorrowDueDateValue = 0;
    
    /** Holds value of firstPeriodeDueDateValue */
    private double firstPeriodeDueDateValue = 0;
    
    /** Holds value of secondPeriodeDueDateValue */
    private double secondPeriodeDueDateValue = 0;
    
    /** Holds value of thirdPeriodeDueDateValue */
    private double thirdPeriodeDueDateValue = 0;
    
    /** Holds value of overPeriodeDueDateValue */
    private double overPeriodeDueDateValue = 0;
    
    /** Holds value of firstPeriodeOverDueValue */
    private double firstPeriodeOverDueValue = 0;
    
    /** Holds value of secondPeriodeOverDueValue */
    private double secondPeriodeOverDueValue = 0;
    
    /** Holds value of thirdPeriodeOverDueValue */
    private double thirdPeriodeOverDueValue = 0;
    
    /** Holds value of overPeriodeOverDueValue */
    private double overPeriodeOverDueValue = 0;

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public double getTodayDueDateValue() {
        return todayDueDateValue;
    }

    public void setTodayDueDateValue(double todayDueDateValue) {
        this.todayDueDateValue = todayDueDateValue;
    }

    public double getTomorrowDueDateValue() {
        return tomorrowDueDateValue;
    }

    public void setTomorrowDueDateValue(double tomorrowDueDateValue) {
        this.tomorrowDueDateValue = tomorrowDueDateValue;
    }

    public double getFirstPeriodeDueDateValue() {
        return firstPeriodeDueDateValue;
    }

    public void setFirstPeriodeDueDateValue(double firstPeriodeDueDateValue) {
        this.firstPeriodeDueDateValue = firstPeriodeDueDateValue;
    }

    public double getSecondPeriodeDueDateValue() {
        return secondPeriodeDueDateValue;
    }

    public void setSecondPeriodeDueDateValue(double secondPeriodeDueDateValue) {
        this.secondPeriodeDueDateValue = secondPeriodeDueDateValue;
    }

    public double getThirdPeriodeDueDateValue() {
        return thirdPeriodeDueDateValue;
    }

    public void setThirdPeriodeDueDateValue(double thirdPeriodeDueDateValue) {
        this.thirdPeriodeDueDateValue = thirdPeriodeDueDateValue;
    }

    public double getOverPeriodeDueDateValue() {
        return overPeriodeDueDateValue;
    }

    public void setOverPeriodeDueDateValue(double overPeriodeDueDateValue) {
        this.overPeriodeDueDateValue = overPeriodeDueDateValue;
    }

    public double getFirstPeriodeOverDueValue() {
        return firstPeriodeOverDueValue;
    }

    public void setFirstPeriodeOverDueValue(double firstPeriodeOverDueValue) {
        this.firstPeriodeOverDueValue = firstPeriodeOverDueValue;
    }

    public double getSecondPeriodeOverDueValue() {
        return secondPeriodeOverDueValue;
    }

    public void setSecondPeriodeOverDueValue(double secondPeriodeOverDueValue) {
        this.secondPeriodeOverDueValue = secondPeriodeOverDueValue;
    }

    public double getThirdPeriodeOverDueValue() {
        return thirdPeriodeOverDueValue;
    }

    public void setThirdPeriodeOverDueValue(double thirdPeriodeOverDueValue) {
        this.thirdPeriodeOverDueValue = thirdPeriodeOverDueValue;
    }

    public double getOverPeriodeOverDueValue() {
        return overPeriodeOverDueValue;
    }

    public void setOverPeriodeOverDueValue(double overPeriodeOverDueValue) {
        this.overPeriodeOverDueValue = overPeriodeOverDueValue;
    }

    public double getBalance(){
         return (this.todayDueDateValue+
                 this.tomorrowDueDateValue+
                 this.firstPeriodeDueDateValue+
                 this.secondPeriodeDueDateValue+
                 this.thirdPeriodeDueDateValue+
                 this.overPeriodeDueDateValue+
                 this.firstPeriodeOverDueValue+
                 this.secondPeriodeOverDueValue+
                 this.thirdPeriodeOverDueValue+
                 this.overPeriodeOverDueValue
        );
    }

    public void mergeByContactId(ArapAgeing other){
        this.todayDueDateValue = this.todayDueDateValue + other.todayDueDateValue;
        this.tomorrowDueDateValue = this.tomorrowDueDateValue + other.tomorrowDueDateValue;
        this.firstPeriodeDueDateValue = this.firstPeriodeDueDateValue + other.firstPeriodeDueDateValue;
        this.secondPeriodeDueDateValue = this.secondPeriodeDueDateValue + other.secondPeriodeDueDateValue;
        this.thirdPeriodeDueDateValue = this.thirdPeriodeDueDateValue + other.thirdPeriodeDueDateValue;
        this.overPeriodeDueDateValue = this.overPeriodeDueDateValue + other.overPeriodeDueDateValue;
        this.firstPeriodeOverDueValue = this.firstPeriodeOverDueValue + other.firstPeriodeOverDueValue;
        this.secondPeriodeOverDueValue = this.secondPeriodeOverDueValue + other.secondPeriodeOverDueValue;
        this.thirdPeriodeOverDueValue = this.thirdPeriodeOverDueValue + other.thirdPeriodeOverDueValue;
        this.overPeriodeOverDueValue = this.overPeriodeOverDueValue + other.overPeriodeOverDueValue;
    }
    
}
