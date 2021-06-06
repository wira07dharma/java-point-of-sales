/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.assignsumberdana;

import com.dimata.qdep.entity.Entity;

public class AssignSumberDana extends Entity {

    private long sumberDanaId = 0;
    private long typeKreditId = 0;
    private float maxPresentasePenggunaan = 0;

    public long getSumberDanaId() {
        return sumberDanaId;
    }

    public void setSumberDanaId(long sumberDanaId) {
        this.sumberDanaId = sumberDanaId;
    }

    public long getTypeKreditId() {
        return typeKreditId;
    }

    public void setTypeKreditId(long typeKreditId) {
        this.typeKreditId = typeKreditId;
    }

    public float getMaxPresentasePenggunaan() {
        return maxPresentasePenggunaan;
    }

    public void setMaxPresentasePenggunaan(float maxPresentasePenggunaan) {
        this.maxPresentasePenggunaan = maxPresentasePenggunaan;
    }

}
