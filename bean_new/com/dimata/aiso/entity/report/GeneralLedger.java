/*
 * GeneralLedger.java
 *
 * Created on July 26, 2005, 1:48 PM
 */

package com.dimata.aiso.entity.report;

import java.util.Date;

/**
 *
 * @author  gedhy, gadnyana
 */ 
public class GeneralLedger {
    private Date glDate;
    private String glNomor = "";
    private String glNama = "";
    private String glKeterangan = "";
    private double glDebet = 0.0;
    private double glKredit = 0.0;
    private double glSaldo = 0.0;
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public long getJurnalOid() {
        return jurnalOid;
    }

    public void setJurnalOid(long jurnalOid) {
        this.jurnalOid = jurnalOid;
    }

    private long jurnalOid = 0;

    public String getNoVoucher() {
        return noVoucher;
    }

    public void setNoVoucher(String noVoucher) {
        this.noVoucher = noVoucher;
    }

    private String noVoucher = "";

    public int getTandaDebetKredit() {
        return tandaDebetKredit;
    }

    public void setTandaDebetKredit(int tandaDebetKredit) {
        this.tandaDebetKredit = tandaDebetKredit;
    }

    private int tandaDebetKredit = 0;

    /**
     * Holds value of property accountNameEnglish.
     */
    private String accountNameEnglish;
    
    public Date getGlDate() {
        return glDate;
    }

    public void setGlDate(Date glDate) {
        this.glDate = glDate;
    }

    public String getGlNomor() {
        return glNomor;
    }

    public void setGlNomor(String glNomor) {
        this.glNomor = glNomor;
    }

    public String getGlNama() {
        return glNama;
    }

    public void setGlNama(String glNama) {
        this.glNama = glNama;
    }

    public String getGlKeterangan() {
        return glKeterangan;
    }

    public void setGlKeterangan(String glKeterangan) {
        this.glKeterangan = glKeterangan;
    }

    public double getGlDebet() {
        return glDebet;
    }

    public void setGlDebet(double glDebet) {
        this.glDebet = glDebet;
    }

    public double getGlKredit() {
        return glKredit;
    }

    public void setGlKredit(double glKredit) {
        this.glKredit = glKredit;
    }

    public double getGlSaldo() {
        return glSaldo;
    }

    public void setGlSaldo(double glSaldo) {
        this.glSaldo = glSaldo;
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
    
    /** Creates a new instance of GeneralLedger */
    public GeneralLedger() {
    }
}
