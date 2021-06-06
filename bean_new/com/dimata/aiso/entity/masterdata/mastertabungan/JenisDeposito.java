/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.mastertabungan;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author HaddyPuutraa
 */
public class JenisDeposito extends Entity{
    private String namaJenisDeposito = "";
    private double minDeposito = 0.0;
    private double maxDeposito = 0.0;
    private double bunga = 0.0;
    private int jangkaWaktu;
    private double provisi = 0.0;
    private double biayaAdmin = 0.0;
    private String keterangan = "";

    public JenisDeposito() {
    }

    /**
     * @return the namaJenisDeposito
     */
    public String getNamaJenisDeposito() {
        return namaJenisDeposito;
    }

    /**
     * @param namaJenisDeposito the namaJenisDeposito to set
     */
    public void setNamaJenisDeposito(String namaJenisDeposito) {
        this.namaJenisDeposito = namaJenisDeposito;
    }

    /**
     * @return the minDeposito
     */
    public double getMinDeposito() {
        return minDeposito;
    }

    /**
     * @param minDeposito the minDeposito to set
     */
    public void setMinDeposito(double minDeposito) {
        this.minDeposito = minDeposito;
    }

    /**
     * @return the maxDeposito
     */
    public double getMaxDeposito() {
        return maxDeposito;
    }

    /**
     * @param maxDeposito the maxDeposito to set
     */
    public void setMaxDeposito(double maxDeposito) {
        this.maxDeposito = maxDeposito;
    }

    /**
     * @return the bunga
     */
    public double getBunga() {
        return bunga;
    }

    /**
     * @param bunga the bunga to set
     */
    public void setBunga(double bunga) {
        this.bunga = bunga;
    }

    /**
     * @return the jangkaWaktu
     */
    public int getJangkaWaktu() {
        return jangkaWaktu;
    }

    /**
     * @param jangkaWaktu the jangkaWaktu to set
     */
    public void setJangkaWaktu(int jangkaWaktu) {
        this.jangkaWaktu = jangkaWaktu;
    }

    /**
     * @return the provisi
     */
    public double getProvisi() {
        return provisi;
    }

    /**
     * @param provisi the provisi to set
     */
    public void setProvisi(double provisi) {
        this.provisi = provisi;
    }

    /**
     * @return the biayaAdmin
     */
    public double getBiayaAdmin() {
        return biayaAdmin;
    }

    /**
     * @param biayaAdmin the biayaAdmin to set
     */
    public void setBiayaAdmin(double biayaAdmin) {
        this.biayaAdmin = biayaAdmin;
    }

    /**
     * @return the keterangan
     */
    public String getKeterangan() {
        return keterangan;
    }

    /**
     * @param keterangan the keterangan to set
     */
    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
    
    
    
}
