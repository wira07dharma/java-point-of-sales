/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class AgunanMapping extends Entity {

    private long agunanId = 0;
    private long pinjamanId = 0;
    private double prosentasePinjaman = 0;

    public long getAgunanId() {
        return agunanId;
    }

    public void setAgunanId(long agunanId) {
        this.agunanId = agunanId;
    }

    public long getPinjamanId() {
        return pinjamanId;
    }

    public void setPinjamanId(long pinjamanId) {
        this.pinjamanId = pinjamanId;
    }

    public double getProsentasePinjaman() {
        return prosentasePinjaman;
    }

    public void setProsentasePinjaman(double prosentasePinjaman) {
        this.prosentasePinjaman = prosentasePinjaman;
    }

}
