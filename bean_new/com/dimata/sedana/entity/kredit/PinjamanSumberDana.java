/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;

public class PinjamanSumberDana extends Entity {

    private long pinjamanId = 0;
    private long sumberDanaId = 0;
    private double jumlahDana = 0;

    public long getPinjamanId() {
        return pinjamanId;
    }

    public void setPinjamanId(long pinjamanId) {
        this.pinjamanId = pinjamanId;
    }

    public long getSumberDanaId() {
        return sumberDanaId;
    }

    public void setSumberDanaId(long sumberDanaId) {
        this.sumberDanaId = sumberDanaId;
    }

    public double getJumlahDana() {
        return jumlahDana;
    }

    public void setJumlahDana(double jumlahDana) {
        this.jumlahDana = jumlahDana;
    }

}
