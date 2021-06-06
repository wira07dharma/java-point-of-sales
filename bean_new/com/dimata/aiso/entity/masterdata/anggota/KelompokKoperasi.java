/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dede
 */
public class KelompokKoperasi extends Entity{
    private String namaKelompok = "" ;
    private String deskripsi = "" ;

    public KelompokKoperasi() {
    }

    /**
     * @return the namaKelompok
     */
    public String getNamaKelompok() {
        return namaKelompok;
    }

    /**
     * @param namaKelompok the namaKelompok to set
     */
    public void setNamaKelompok(String namaKelompok) {
        this.namaKelompok = namaKelompok;
    }

    /**
     * @return the deskripsi
     */
    public String getDeskripsi() {
        return deskripsi;
    }

    /**
     * @param deskripsi the deskripsi to set
     */
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
