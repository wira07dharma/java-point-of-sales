/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class AnggotaKeluarga extends Entity {

    private long contactAnggotaId = 0;
    private long contactKeluargaId = 0;
    private int statusRelasi = 0;
    private String keterangan = "";

    public long getContactAnggotaId() {
        return contactAnggotaId;
    }

    public void setContactAnggotaId(long contactAnggotaId) {
        this.contactAnggotaId = contactAnggotaId;
    }

    public long getContactKeluargaId() {
        return contactKeluargaId;
    }

    public void setContactKeluargaId(long contactKeluargaId) {
        this.contactKeluargaId = contactKeluargaId;
    }

    public int getStatusRelasi() {
        return statusRelasi;
    }

    public void setStatusRelasi(int statusRelasi) {
        this.statusRelasi = statusRelasi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

}
