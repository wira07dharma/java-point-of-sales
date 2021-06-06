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

public class DetailTransaksi extends Entity {

    private long jenisTransaksiId = 0;
    private double debet = 0;
    private double kredit = 0;
    private long idSimpanan = 0;
    private long transaksiId = 0;
    private Date tglPrintTerakhir = null;
    private String detailInfo = "";

    public long getJenisTransaksiId() {
        return jenisTransaksiId;
    }

    public void setJenisTransaksiId(long jenisTransaksiId) {
        this.jenisTransaksiId = jenisTransaksiId;
    }

    public double getDebet() {
        return debet;
    }

    public void setDebet(double debet) {
        this.debet = debet;
    }

    public double getKredit() {
        return kredit;
    }

    public void setKredit(double kredit) {
        this.kredit = kredit;
    }

    public long getIdSimpanan() {
        return idSimpanan;
    }

    public void setIdSimpanan(long idSimpanan) {
        this.idSimpanan = idSimpanan;
    }

    public Date getTglPrintTerakhir() {
        return tglPrintTerakhir;
    }

    public void setTglPrintTerakhir(Date tglPrintTerakhir) {
        this.tglPrintTerakhir = tglPrintTerakhir;
    }

    /**
     * @return the transaksiId
     */
    public long getTransaksiId() {
        return transaksiId;
    }

    /**
     * @param transaksiId the transaksiId to set
     */
    public void setTransaksiId(long transaksiId) {
        this.transaksiId = transaksiId;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

}
