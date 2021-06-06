/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class MasterTabunganPenarikan extends Entity {

    private String judulRangePenarikan = "";
    private int tipeRange = 0;
    private Date startDate = null;
    private Date endDate = null;
    private int periodeBulan = 0;

    public String getJudulRangePenarikan() {
        return judulRangePenarikan;
    }

    public void setJudulRangePenarikan(String judulRangePenarikan) {
        this.judulRangePenarikan = judulRangePenarikan;
    }

    public int getTipeRange() {
        return tipeRange;
    }

    public void setTipeRange(int tipeRange) {
        this.tipeRange = tipeRange;
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

    public int getPeriodeBulan() {
        return periodeBulan;
    }

    public void setPeriodeBulan(int periodeBulan) {
        this.periodeBulan = periodeBulan;
    }

}
