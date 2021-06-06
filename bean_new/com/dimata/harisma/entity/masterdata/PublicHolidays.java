/**
 * User: wardana
 * Date: Apr 8, 2004
 * Time: 8:46:01 AM
 * Version: 1.0 
 */
package com.dimata.harisma.entity.masterdata;

import com.dimata.qdep.entity.Entity;

import java.util.Date;

public class PublicHolidays extends Entity {

    private Date dtHolidayDate;
    private String stDesc = "";
    private int iHolidaySts;

    public PublicHolidays() {
    }

    public Date getDtHolidayDate() {
        return dtHolidayDate;
    }

    public void setDtHolidayDate(Date dtHolidayDate) {
        this.dtHolidayDate = dtHolidayDate;
    }

    public String getStDesc() {
        return stDesc;
    }

    public void setStDesc(String stDesc) {
        this.stDesc = stDesc;
    }

    public int getiHolidaySts() {
        return iHolidaySts;
    }

    public void setiHolidaySts(int iHolidaySts) {
        this.iHolidaySts = iHolidaySts;
    }
}
