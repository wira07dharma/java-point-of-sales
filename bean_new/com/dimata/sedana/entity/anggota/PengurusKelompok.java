/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.anggota;

import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.common.I_Sedana;

/**
 *
 * @author Dimata 007
 */
public class PengurusKelompok extends Entity implements I_Sedana{
    
    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;
    
    public static final String[] GENDER_TITLE = {"Laki-laki", "Perempuan"};
    
    public static final int STATUS_KEPEMILIKAN_TIDAK_AKTIF = 0;
    public static final int STATUS_KEPEMILIKAN_AKTIF = 1;
    
    public static final String[] STATUS_KEPEMILIKAN_TITLE = {"Tidak Aktif", "Aktif"};
    
    private String namaPengurus = "";
    private int jenisKelamin = 0;
    private int statusKepemilikan = 0;
    private long jabatan = 0;
    private String telepon = "";
    private String email = "";
    private String alamat = "";
    private double prosentaseKepengurusan = 0;
    private long idKelompok = 0;

    public long getIdKelompok() {
        return idKelompok;
    }

    public void setIdKelompok(long idKelompok) {
        this.idKelompok = idKelompok;
    }

    public String getNamaPengurus() {
        return namaPengurus;
    }

    public void setNamaPengurus(String namaPengurus) {
        this.namaPengurus = namaPengurus;
    }

    public int getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(int jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public int getStatusKepemilikan() {
        return statusKepemilikan;
    }

    public void setStatusKepemilikan(int statusKepemilikan) {
        this.statusKepemilikan = statusKepemilikan;
    }

    public long getJabatan() {
        return jabatan;
    }

    public void setJabatan(long jabatan) {
        this.jabatan = jabatan;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public double getProsentaseKepengurusan() {
        return prosentaseKepengurusan;
    }

    public void setProsentaseKepengurusan(double prosentaseKepengurusan) {
        this.prosentaseKepengurusan = prosentaseKepengurusan;
    }

}
