/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.mastertabungan;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dw1p4
 */
public class TabunganBerjangka extends Entity {
    private String name="";
    private double nilaiTabungan=0;
    private double prosentaseNilai=0;
    
    public TabunganBerjangka(){
    }
    
    public String getName () {
        return name;
    }
    public double getNilaiTabungan () {
        return nilaiTabungan;
    }
    public double getProsentaseNilai () {
        return prosentaseNilai;
    }
    
    public void setName(String Name) {
        this.name = Name;
    }
    public void setNilaiTabungan(double NilaiTabungan) {
        this.nilaiTabungan = NilaiTabungan;
    }
    public void setProsentaseNilai(double ProsentaseNilai) {
        this.prosentaseNilai = ProsentaseNilai;
    }
    
}
