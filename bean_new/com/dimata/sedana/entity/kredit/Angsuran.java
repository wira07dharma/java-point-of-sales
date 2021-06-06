/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class Angsuran extends Entity {

    private double jumlahAngsuran = 0;
    private long jadwalAngsuranId = 0;
    private long transaksiId = 0;    

    //tambahan setter getter untuk report (non-database)
    private double totalDibayar = 0;
    private Date tunggakanBulanAwal = null;

    public double getJumlahAngsuran() {
        return jumlahAngsuran;
    }

    public void setJumlahAngsuran(double jumlahAngsuran) {
        this.jumlahAngsuran = jumlahAngsuran;
    }

    public long getJadwalAngsuranId() {
        return jadwalAngsuranId;
    }

    public void setJadwalAngsuranId(long jadwalAngsuranId) {
        this.jadwalAngsuranId = jadwalAngsuranId;
    }

    public long getTransaksiId() {
        return transaksiId;
    }

    public void setTransaksiId(long transaksiId) {
        this.transaksiId = transaksiId;
    }

    //tambahan setter getter untuk report (non-database)
    public double getTotalDibayar() {
        return totalDibayar;
    }

    public void setTotalDibayar(double totalDibayar) {
        this.totalDibayar = totalDibayar;
    }

    public Date getTunggakanBulanAwal() {
        return tunggakanBulanAwal;
    }

    public void setTunggakanBulanAwal(Date tunggakanBulanAwal) {
        this.tunggakanBulanAwal = tunggakanBulanAwal;
    }

}
