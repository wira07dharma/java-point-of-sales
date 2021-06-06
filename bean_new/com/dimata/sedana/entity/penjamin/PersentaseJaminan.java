/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.penjamin;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class PersentaseJaminan extends Entity {

    private long idPenjamin = 0;
    private int jangkaWaktu = 0;
    private double persentaseCoverage = 0;
    private double persentaseDijamin = 0;

    public long getIdPenjamin() {
        return idPenjamin;
    }

    public void setIdPenjamin(long idPenjamin) {
        this.idPenjamin = idPenjamin;
    }

    public int getJangkaWaktu() {
        return jangkaWaktu;
    }

    public void setJangkaWaktu(int jangkaWaktu) {
        this.jangkaWaktu = jangkaWaktu;
    }

    public double getPersentaseCoverage() {
        return persentaseCoverage;
    }

    public void setPersentaseCoverage(double persentaseCoverage) {
        this.persentaseCoverage = persentaseCoverage;
    }

    public double getPersentaseDijamin() {
        return persentaseDijamin;
    }

    public void setPersentaseDijamin(double persentaseDijamin) {
        this.persentaseDijamin = persentaseDijamin;
    }

}
