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
import com.dimata.sedana.common.I_Sedana;

public class Transaksi extends Entity implements I_Sedana {

    private Date tanggalTransaksi = null;
    private String kodeBuktiTransaksi = "";
    private long idAnggota = 0;
    private Date tglPrintTerakhir = null;
    private long tellerShiftId = 0;
    private String keterangan = "";
    private int status = 0;
    private int tipeArusKas = 0;
    private long pinjamanId = 0;
    private long idDeposito = 0;
    private int usecaseType = 0;
    private long transaksiParentId = 0;

    public Date getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(Date tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public String getKodeBuktiTransaksi() {
        return kodeBuktiTransaksi;
    }

    public void setKodeBuktiTransaksi(String kodeBuktiTransaksi) {
        this.kodeBuktiTransaksi = kodeBuktiTransaksi;
    }

    public long getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(long idAnggota) {
        this.idAnggota = idAnggota;
    }

    public Date getTglPrintTerakhir() {
        return tglPrintTerakhir;
    }

    public void setTglPrintTerakhir(Date tglPrintTerakhir) {
        this.tglPrintTerakhir = tglPrintTerakhir;
    }

    public long getTellerShiftId() {
        return tellerShiftId;
    }

    public void setTellerShiftId(long tellerShiftId) {
        this.tellerShiftId = tellerShiftId;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the tipeArusKas
     */
    public int getTipeArusKas() {
        return tipeArusKas;
    }

    /**
     * @param tipeArusKas the tipeArusKas to set
     */
    public void setTipeArusKas(int tipeArusKas) {
        this.tipeArusKas = tipeArusKas;
    }

    /**
     * @return the pinjamanId
     */
    public long getPinjamanId() {
        return pinjamanId;
    }

    /**
     * @param pinjamanId the pinjamanId to set
     */
    public void setPinjamanId(long pinjamanId) {
        this.pinjamanId = pinjamanId;
    }

    /**
     * @return the idDeposito
     */
    public long getIdDeposito() {
        return idDeposito;
    }

    /**
     * @param idDeposito the idDeposito to set
     */
    public void setIdDeposito(long idDeposito) {
        this.idDeposito = idDeposito;
    }

    public int getUsecaseType() {
        return usecaseType;
    }

    public void setUsecaseType(int usecaseType) {
        this.usecaseType = usecaseType;
    }

    public long getTransaksiParentId() {
        return transaksiParentId;
    }

    public void setTransaksiParentId(long transaksiParentId) {
        this.transaksiParentId = transaksiParentId;
    }

}
