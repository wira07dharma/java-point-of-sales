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
public class Deposito extends Entity{
    private long idAnggota;
    private long idJenisDeposito;
    private long idKelompokKoperasi;
    private Date tanggalPengajuanDeposito = new Date();
    private double jumlahDeposito = 0.0;
    private int status;

    /**
     * @return the idAnggota
     */
    public long getIdAnggota() {
        return idAnggota;
    }

    /**
     * @param idAnggota the idAnggota to set
     */
    public void setIdAnggota(long idAnggota) {
        this.idAnggota = idAnggota;
    }

    /**
     * @return the idJenisDeposito
     */
    public long getIdJenisDeposito() {
        return idJenisDeposito;
    }

    /**
     * @param idJenisDeposito the idJenisDeposito to set
     */
    public void setIdJenisDeposito(long idJenisDeposito) {
        this.idJenisDeposito = idJenisDeposito;
    }

    /**
     * @return the idKelompokKoperasi
     */
    public long getIdKelompokKoperasi() {
        return idKelompokKoperasi;
    }

    /**
     * @param idKelompokKoperasi the idKelompokKoperasi to set
     */
    public void setIdKelompokKoperasi(long idKelompokKoperasi) {
        this.idKelompokKoperasi = idKelompokKoperasi;
    }

    /**
     * @return the tanggalPengajuanDeposito
     */
    public Date getTanggalPengajuanDeposito() {
        return tanggalPengajuanDeposito;
    }

    /**
     * @param tanggalPengajuanDeposito the tanggalPengajuanDeposito to set
     */
    public void setTanggalPengajuanDeposito(Date tanggalPengajuanDeposito) {
        this.tanggalPengajuanDeposito = tanggalPengajuanDeposito;
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
     * @return the jumlahDeposito
     */
    public double getJumlahDeposito() {
        return jumlahDeposito;
    }

    /**
     * @param jumlahDeposito the jumlahDeposito to set
     */
    public void setJumlahDeposito(double jumlahDeposito) {
        this.jumlahDeposito = jumlahDeposito;
    }
    
    
}
