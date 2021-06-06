/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class DataTabungan extends Entity {

    private long idAnggota = 0;
    private long idJenisSimpanan = 0;
    private Date tanggal = null;
    private int status = 0;
    private String kodeTabungan = "";
    private long contactIdAhliWaris = 0;
    private Date tanggalTutup = null;
    private String alasanTutup = "";
    private String catatan = "";
    private long assignTabunganId = 0;
    private long idJenisTabungan = 0;
    private float jumlah = 0;
    //added by dewok 20181031 for alokasi bunga deposito
    private long idAlokasiBunga = 0;

    public long getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(long idAnggota) {
        this.idAnggota = idAnggota;
    }

    public long getIdJenisSimpanan() {
        return idJenisSimpanan;
    }

    public void setIdJenisSimpanan(long idJenisSimpanan) {
        this.idJenisSimpanan = idJenisSimpanan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKodeTabungan() {
        return kodeTabungan;
    }

    public void setKodeTabungan(String kodeTabungan) {
        this.kodeTabungan = kodeTabungan;
    }

    public long getContactIdAhliWaris() {
        return contactIdAhliWaris;
    }

    public void setContactIdAhliWaris(long contactIdAhliWaris) {
        this.contactIdAhliWaris = contactIdAhliWaris;
    }

    public Date getTanggalTutup() {
        return tanggalTutup;
    }

    public void setTanggalTutup(Date tanggalTutup) {
        this.tanggalTutup = tanggalTutup;
    }

    public String getAlasanTutup() {
        return alasanTutup;
    }

    public void setAlasanTutup(String alasanTutup) {
        this.alasanTutup = alasanTutup;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    /**
     * @return the assignTabunganId
     */
    public long getAssignTabunganId() {
        return assignTabunganId;
    }

    /**
     * @param assignTabunganId the assignTabunganId to set
     */
    public void setAssignTabunganId(long assignTabunganId) {
        this.assignTabunganId = assignTabunganId;
    }

    /**
     * @return the idJenisTabungan
     */
    public long getIdJenisTabungan() {
        return idJenisTabungan;
    }

    /**
     * @param idJenisTabungan the idJenisTabungan to set
     */
    public void setIdJenisTabungan(long idJenisTabungan) {
        this.idJenisTabungan = idJenisTabungan;
    }

    /**
     * @return the jumlah
     */
    public float getJumlah() {
        return jumlah;
    }

    /**
     * @param jumlah the jumlah to set
     */
    public void setJumlah(float jumlah) {
        this.jumlah = jumlah;
    }

    public long getIdAlokasiBunga() {
        return idAlokasiBunga;
    }

    public void setIdAlokasiBunga(long idAlokasiBunga) {
        this.idAlokasiBunga = idAlokasiBunga;
    }

}
