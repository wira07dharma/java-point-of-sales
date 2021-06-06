/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class CalcCogsMain extends Entity {

    private Date costDateStart = null;
    private Date costDateEnd = null;
    private Date salesDateStart = null;
    private Date salesDateEnd = null;
    private int status = 0;
    private String note = "";

    public Date getCostDateStart() {
        return costDateStart;
    }

    public void setCostDateStart(Date costDateStart) {
        this.costDateStart = costDateStart;
    }

    public Date getCostDateEnd() {
        return costDateEnd;
    }

    public void setCostDateEnd(Date costDateEnd) {
        this.costDateEnd = costDateEnd;
    }

    public Date getSalesDateStart() {
        return salesDateStart;
    }

    public void setSalesDateStart(Date salesDateStart) {
        this.salesDateStart = salesDateStart;
    }

    public Date getSalesDateEnd() {
        return salesDateEnd;
    }

    public void setSalesDateEnd(Date salesDateEnd) {
        this.salesDateEnd = salesDateEnd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
