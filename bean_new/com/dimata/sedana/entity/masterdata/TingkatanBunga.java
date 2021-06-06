/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class TingkatanBunga extends Entity {

    private long idJenisSimpana = 0;
    private double nominalSaldoMin = 0;
    private double persentaseBunga = 0;

    public long getIdJenisSimpanan() {
        return idJenisSimpana;
    }

    public void setIdJenisSimpanan(long idJenisSimpana) {
        this.idJenisSimpana = idJenisSimpana;
    }

    public double getNominalSaldoMin() {
        return nominalSaldoMin;
    }

    public void setNominalSaldoMin(double nominalSaldoMin) {
        this.nominalSaldoMin = nominalSaldoMin;
    }

    public double getPersentaseBunga() {
        return persentaseBunga;
    }

    public void setPersentaseBunga(double persentaseBunga) {
        this.persentaseBunga = persentaseBunga;
    }

}
