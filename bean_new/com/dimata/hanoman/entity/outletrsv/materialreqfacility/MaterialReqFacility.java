/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.outletrsv.materialreqfacility;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class MaterialReqFacility extends Entity {

    private long materialReqLocationId = 0;
    private long aktivaId = 0;
    private long materialId = 0;
    private float number = 0;
    private String note = "";
    private int orderIndex = 0;
    private float duration = 0;

    public long getMaterialReqLocationId() {
        return materialReqLocationId;
    }

    public void setMaterialReqLocationId(long materialReqLocationId) {
        this.materialReqLocationId = materialReqLocationId;
    }

    public long getAktivaId() {
        return aktivaId;
    }

    public void setAktivaId(long aktivaId) {
        this.aktivaId = aktivaId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

}
