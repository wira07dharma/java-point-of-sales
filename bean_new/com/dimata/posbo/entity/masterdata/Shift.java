package com.dimata.posbo.entity.masterdata;

/* package java */

import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Shift extends Entity {

    private String name = "";
    private String remark = "";
    private Date startTime = new Date();
    private Date endTime = new Date();

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        if (remark == null) {
            remark = "";
        }
        this.remark = remark;
    }


}
