/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.rsvcustpackschedule;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dewa
 */
public class ResvCustomPackSchedule extends Entity {

    private long customePackBillingId = 0;
    private long roomId = 0;
    private long tableId = 0;
    private Date startDate = null;
    private Date endDate = null;
    private int status = 0;
    private double quantity = 0;
    private String roomName = "";

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public long getCustomePackBillingId() {
        return customePackBillingId;
    }

    public void setCustomePackBillingId(long customePackBillingId) {
        this.customePackBillingId = customePackBillingId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
