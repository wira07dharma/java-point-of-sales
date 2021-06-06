/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.transaksi;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author HaddyPuutraa
 */
public class Angsuran extends Entity {

    private long idPinjaman;
    private Date tanggalAngsuran = new Date();
    private double jumlahAngsuran = 0.0;
    private long jadwalAngsuranId = 0;
    private long tellerShiftId = 0;
    private String kodeBuktiBayar = "";
    private Date tglPrintTerakhir = null;

    public Angsuran() {
    }

    /**
     * @return the idPinjaman
     */
    public long getIdPinjaman() {
        return idPinjaman;
    }

    /**
     * @param idPinjaman the idPinjaman to set
     */
    public void setIdPinjaman(long idPinjaman) {
        this.idPinjaman = idPinjaman;
    }

    /**
     * @return the tanggalAngsuran
     */
    public Date getTanggalAngsuran() {
        return tanggalAngsuran;
    }

    /**
     * @param tanggalAngsuran the tanggalAngsuran to set
     */
    public void setTanggalAngsuran(Date tanggalAngsuran) {
        this.tanggalAngsuran = tanggalAngsuran;
    }

    /**
     * @return the jumlahAngsuran
     */
    public double getJumlahAngsuran() {
        return jumlahAngsuran;
    }

    /**
     * @param jumlahAngsuran the jumlahAngsuran to set
     */
    public void setJumlahAngsuran(double jumlahAngsuran) {
        this.jumlahAngsuran = jumlahAngsuran;
    }

    public long getJadwalAngsuranId() {
        return jadwalAngsuranId;
    }

    public void setJadwalAngsuranId(long jadwalAngsuranId) {
        this.jadwalAngsuranId = jadwalAngsuranId;
    }

    public long getTellerShiftId() {
        return tellerShiftId;
    }

    public void setTellerShiftId(long tellerShiftId) {
        this.tellerShiftId = tellerShiftId;
    }

    public String getKodeBuktiBayar() {
        return kodeBuktiBayar;
    }

    public void setKodeBuktiBayar(String kodeBuktiBayar) {
        this.kodeBuktiBayar = kodeBuktiBayar;
    }

    public Date getTglPrintTerakhir() {
        return tglPrintTerakhir;
    }

    public void setTglPrintTerakhir(Date tglPrintTerakhir) {
        this.tglPrintTerakhir = tglPrintTerakhir;
    }

}
