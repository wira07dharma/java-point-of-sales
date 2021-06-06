/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Nov 8, 2005
 * Time: 2:54:32 PM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.entity.report;
 
import java.util.Date;

public class AktivaSusut {
    private String namaaktiva = "";
    private String kode = "";
    private Date tglPerolehan = new Date();
    private int masaManfaat = 0;
    private double mutasiProlehBulanLalu = 0.0;
    private double mutasiTambah = 0.0;
    private double mustasiKurang = 0.0;
    private double totalBulanIni = 0.0;
    private double susutBulanlalu = 0.0;
    private double susutTambah = 0.0;
    private double susutKurang = 0.0;
    private double susutBulanIni = 0.0;
    private double totalSusutBulanIni = 0.0;
    private double nilaiBuku = 0;

    /**
     * Holds value of property locationId.
     */
    private long locationId = 0;
    
    /**
     * Holds value of property quantity.
     */
    private int quantity = 0;
    
    public double getNilaiBuku() {
        return nilaiBuku;
    }

    public void setNilaiBuku(double nilaiBuku) {
        this.nilaiBuku = nilaiBuku;
    }

    public double getSusutBulanIni() {
        return susutBulanIni;
    }

    public void setSusutBulanIni(double susutBulanIni) {
        this.susutBulanIni = susutBulanIni;
    }

    public double getTotalSusutBulanIni() {
        return totalSusutBulanIni;
    }

    public void setTotalSusutBulanIni(double totalSusutBulanIni) {
        this.totalSusutBulanIni = totalSusutBulanIni;
    }

    public String getNamaaktiva() {
        return namaaktiva;
    }

    public void setNamaaktiva(String namaaktiva) {
        this.namaaktiva = namaaktiva;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public Date getTglPerolehan() {
        return tglPerolehan;
    }

    public void setTglPerolehan(Date tglPerolehan) {
        this.tglPerolehan = tglPerolehan;
    }

    public int getMasaManfaat() {
        return masaManfaat;
    }

    public void setMasaManfaat(int masaManfaat) {
        this.masaManfaat = masaManfaat;
    }

    public double getMutasiProlehBulanLalu() {
        return mutasiProlehBulanLalu;
    }

    public void setMutasiProlehBulanLalu(double mutasiProlehBulanLalu) {
        this.mutasiProlehBulanLalu = mutasiProlehBulanLalu;
    }

    public double getMutasiTambah() {
        return mutasiTambah;
    }

    public void setMutasiTambah(double mutasiTambah) {
        this.mutasiTambah = mutasiTambah;
    }

    public double getMustasiKurang() {
        return mustasiKurang;
    }

    public void setMustasiKurang(double mustasiKurang) {
        this.mustasiKurang = mustasiKurang;
    }

    public double getTotalBulanIni() {
        return totalBulanIni;
    }

    public void setTotalBulanIni(double totalBulanIni) {
        this.totalBulanIni = totalBulanIni;
    }

    public double getSusutBulanlalu() {
        return susutBulanlalu;
    }

    public void setSusutBulanlalu(double susutBulanlalu) {
        this.susutBulanlalu = susutBulanlalu;
    }

    public double getSusutTambah() {
        return susutTambah;
    }

    public void setSusutTambah(double susutTambah) {
        this.susutTambah = susutTambah;
    }

    public double getSusutKurang() {
        return susutKurang;
    }

    public void setSusutKurang(double susutKurang) {
        this.susutKurang = susutKurang;
    }

    /**
     * Getter for property locationId.
     * @return Value of property locationId.
     */
    public long getLocationId() {
        return this.locationId;
    }
    
    /**
     * Setter for property locationId.
     * @param locationId New value of property locationId.
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    /**
     * Getter for property quantity.
     * @return Value of property quantity.
     */
    public int getQuantity() {
        return this.quantity;
    }
    
    /**
     * Setter for property quantity.
     * @param quantity New value of property quantity.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}
