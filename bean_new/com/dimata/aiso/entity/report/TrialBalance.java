/*
 * TrialBalance.java
 *
 * Created on July 26, 2005, 1:48 PM
 */

package com.dimata.aiso.entity.report;

/**
 *
 * @author  gedhy, gadnyana
 */
public class TrialBalance {

    private String nomor = "";
    private String nama = "";
    private double anggaran = 0.0;
    private double saldoAwal = 0.0;
    private double debet = 0.0;
    private double kredit = 0.0;
    private double saldoAkhir = 0.0;

    public int getTandaDebetKredit() {
        return TandaDebetKredit;
    }

    public void setTandaDebetKredit(int tandaDebetKredit) {
        TandaDebetKredit = tandaDebetKredit;
    }

    private int TandaDebetKredit = 0;

    /**
     * Holds value of property accountNameEnglish.
     */
    private String accountNameEnglish;
    
    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getAnggaran() {
        return anggaran;
    }

    public void setAnggaran(double anggaran) {
        this.anggaran = anggaran;
    }

    public double getSaldoAwal() {
        return saldoAwal;
    }

    public void setSaldoAwal(double saldoAwal) {
        this.saldoAwal = saldoAwal;
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

    public double getSaldoAkhir() {
        return saldoAkhir;
    }

    public void setSaldoAkhir(double saldoAkhir) {
        this.saldoAkhir = saldoAkhir;
    }

    /**
     * Getter for property accountNameEnglish.
     * @return Value of property accountNameEnglish.
     */
    public String getAccountNameEnglish() {
        return this.accountNameEnglish;
    }
    
    /**
     * Setter for property accountNameEnglish.
     * @param accountNameEnglish New value of property accountNameEnglish.
     */
    public void setAccountNameEnglish(String accountNameEnglish) {
        this.accountNameEnglish = accountNameEnglish;
    }
    
    /** Creates a new instance of TrialBalance */
    public TrialBalance() {
    }
    
}
