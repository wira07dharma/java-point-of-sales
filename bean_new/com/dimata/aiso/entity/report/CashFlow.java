/*
 * CashFlow.java
 *
 * Created on July 26, 2005, 1:48 PM
 */

package com.dimata.aiso.entity.report;    
import java.util.*;

/**
 *
 * @author  gedhy, gad
 */
public class CashFlow {
    private String noVoucher = "";
    private String Keterangan = "";
    private double value = 0.0;
    private Date tglTransaksi; 

    /**
     * Holds value of property noDocRef.
     */
    private String noDocRef = "";
    
    /**
     * Holds value of property noPerkiraan.
     */
    private String noPerkiraan = "";
    
    /**
     * Holds value of property namaPerkiraan.
     */
    private String namaPerkiraan = "";
    
    /**
     * Holds value of property accountNameEnglish.
     */
    private String accountNameEnglish;
    
    /**
     * Holds value of property idPerkiraan.
     */
    private long idPerkiraan = 0;
    
    public String getNoVoucher() {
        return noVoucher;
    }

    public void setNoVoucher(String noVoucher) {
        this.noVoucher = noVoucher;
    }

    public String getKeterangan() {
        return Keterangan; 
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    
    public Date getTglTransaksi() {
        return tglTransaksi;
    }

    public void setTglTransaksi(Date tglTransaksi) {
        this.tglTransaksi = tglTransaksi;
    }
    
    /**
     * Getter for property noDocRef.
     * @return Value of property noDocRef.
     */
    public String getNoDocRef() {
        return this.noDocRef;
    }
    
    /**
     * Setter for property noDocRef.
     * @param noDocRef New value of property noDocRef.
     */
    public void setNoDocRef(String noDocRef) {
        this.noDocRef = noDocRef;
    }
    
    /**
     * Getter for property noPerkiraan.
     * @return Value of property noPerkiraan.
     */
    public String getNoPerkiraan() {
        return this.noPerkiraan;
    }
    
    /**
     * Setter for property noPerkiraan.
     * @param noPerkiraan New value of property noPerkiraan.
     */
    public void setNoPerkiraan(String noPerkiraan) {
        this.noPerkiraan = noPerkiraan;
    }
    
    /**
     * Getter for property namaPerkiraan.
     * @return Value of property namaPerkiraan.
     */
    public String getNamaPerkiraan() {
        return this.namaPerkiraan;
    }
    
    /**
     * Setter for property namaPerkiraan.
     * @param namaPerkiraan New value of property namaPerkiraan.
     */
    public void setNamaPerkiraan(String namaPerkiraan) {
        this.namaPerkiraan = namaPerkiraan;
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
    
    /**
     * Getter for property idPerkiraan.
     * @return Value of property idPerkiraan.
     */
    public long getIdPerkiraan() {
        return this.idPerkiraan;
    }
    
    /**
     * Setter for property idPerkiraan.
     * @param idPerkiraan New value of property idPerkiraan.
     */
    public void setIdPerkiraan(long idPerkiraan) {
        this.idPerkiraan = idPerkiraan;
    }
    
}
