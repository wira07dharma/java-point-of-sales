/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class Kadar extends Entity {

    private String kodeKadar = "";
    private double kadar = 0 ;
    private int karat = 0;

    /**
     * @return the kodeKadar
     */
    public String getKodeKadar() {
        return kodeKadar;
    }

    /**
     * @param kodeKadar the kodeKadar to set
     */
    public void setKodeKadar(String kodeKadar) {
        this.kodeKadar = kodeKadar;
    }

    /**
     * @return the kadar
     */
    public double getKadar() {
        return kadar;
    }

    /**
     * @param kadar the kadar to set
     */
    public void setKadar(double kadar) {
        this.kadar = kadar;
    }

    /**
     * @return the karat
     */
    public int getKarat() {
        return karat;
    }

    /**
     * @param karat the karat to set
     */
    public void setKarat(int karat) {
        this.karat = karat;
    }

   

}
