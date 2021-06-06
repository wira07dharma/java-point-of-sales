/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.sumberdana;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class SumberDana extends Entity {

    private long contactId = 0;
    private int jenisSumberDana = 0;
    private String kodeSumberDana = "";
    private String judulSumberDana = "";
    private String targetPendanaan = "";
    private int prioritasPenggunaan = 0;
    private double totalKetersediaanDana = 0;
    private float biayaBungaKeKreditur = 0;
    private int tipeBungaKeKreditur = 0;
    private Date tanggalDanaMasuk = null;
    private Date tanggalLunasKeKreditur = null;
    private Date tanggalDanaMulaiTersedia = null;
    private Date tanggalAkhirTersedia = null;
    private double minimumPinjamanKeDebitur = 0;
    private double maximumPinjamanKeDebitur = 0;

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public int getJenisSumberDana() {
        return jenisSumberDana;
    }

    public void setJenisSumberDana(int jenisSumberDana) {
        this.jenisSumberDana = jenisSumberDana;
    }

    public String getKodeSumberDana() {
        return kodeSumberDana;
    }

    public void setKodeSumberDana(String kodeSumberDana) {
        this.kodeSumberDana = kodeSumberDana;
    }

    public String getJudulSumberDana() {
        return judulSumberDana;
    }

    public void setJudulSumberDana(String judulSumberDana) {
        this.judulSumberDana = judulSumberDana;
    }

    public String getTargetPendanaan() {
        return targetPendanaan;
    }

    public void setTargetPendanaan(String targetPendanaan) {
        this.targetPendanaan = targetPendanaan;
    }

    public int getPrioritasPenggunaan() {
        return prioritasPenggunaan;
    }

    public void setPrioritasPenggunaan(int prioritasPenggunaan) {
        this.prioritasPenggunaan = prioritasPenggunaan;
    }

    public double getTotalKetersediaanDana() {
        return totalKetersediaanDana;
    }

    public void setTotalKetersediaanDana(double totalKetersediaanDana) {
        this.totalKetersediaanDana = totalKetersediaanDana;
    }

    public float getBiayaBungaKeKreditur() {
        return biayaBungaKeKreditur;
    }

    public void setBiayaBungaKeKreditur(float biayaBungaKeKreditur) {
        this.biayaBungaKeKreditur = biayaBungaKeKreditur;
    }

    public int getTipeBungaKeKreditur() {
        return tipeBungaKeKreditur;
    }

    public void setTipeBungaKeKreditur(int tipeBungaKeKreditur) {
        this.tipeBungaKeKreditur = tipeBungaKeKreditur;
    }

    public Date getTanggalDanaMasuk() {
        return tanggalDanaMasuk;
    }

    public void setTanggalDanaMasuk(Date tanggalDanaMasuk) {
        this.tanggalDanaMasuk = tanggalDanaMasuk;
    }

    public Date getTanggalLunasKeKreditur() {
        return tanggalLunasKeKreditur;
    }

    public void setTanggalLunasKeKreditur(Date tanggalLunasKeKreditur) {
        this.tanggalLunasKeKreditur = tanggalLunasKeKreditur;
    }

    public Date getTanggalDanaMulaiTersedia() {
        return tanggalDanaMulaiTersedia;
    }

    public void setTanggalDanaMulaiTersedia(Date tanggalDanaMulaiTersedia) {
        this.tanggalDanaMulaiTersedia = tanggalDanaMulaiTersedia;
    }

    public Date getTanggalAkhirTersedia() {
        return tanggalAkhirTersedia;
    }

    public void setTanggalAkhirTersedia(Date tanggalAkhirTersedia) {
        this.tanggalAkhirTersedia = tanggalAkhirTersedia;
    }

    public double getMinimumPinjamanKeDebitur() {
        return minimumPinjamanKeDebitur;
    }

    public void setMinimumPinjamanKeDebitur(double minimumPinjamanKeDebitur) {
        this.minimumPinjamanKeDebitur = minimumPinjamanKeDebitur;
    }

    public double getMaximumPinjamanKeDebitur() {
        return maximumPinjamanKeDebitur;
    }

    public void setMaximumPinjamanKeDebitur(double maximumPinjamanKeDebitur) {
        this.maximumPinjamanKeDebitur = maximumPinjamanKeDebitur;
    }

}
