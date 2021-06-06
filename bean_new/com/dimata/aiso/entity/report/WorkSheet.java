/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Aug 5, 2005
 * Time: 9:37:35 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.aiso.entity.report;

public class WorkSheet { 
    private String nomorPerkiraan = "";
    private String namaPerkiraan = "";
    private double anggaran = 0.0;
    private double debetNeracaPeriodeLalu = 0.0;
    private double kreditNeracaPeriodeLalu = 0.0;
    private double debetMutasi = 0.0;
    private double kreditMutasi = 0.0;
    private double debetNeracaSaldo = 0.0;
    private double kreditNeracaSaldo = 0.0;
    private double debetLabaRugi = 0.0;
    private double kreditLabaRugi = 0.0;
    private double debetNeraca = 0.0;
    private double kreditNeraca = 0.0;

    public int getTandaDebetKredit() {
        return tandaDebetKredit;
    }

    public void setTandaDebetKredit(int tandaDebetKredit) {
        this.tandaDebetKredit = tandaDebetKredit;
    }

    private int tandaDebetKredit = 1;

    /**
     * Holds value of property accountNameEnglish.
     */
    private String accountNameEnglish;
    
    public String getNomorPerkiraan() {
        return nomorPerkiraan;
    }

    public void setNomorPerkiraan(String nomorPerkiraan) {
        this.nomorPerkiraan = nomorPerkiraan;
    }

    public String getNamaPerkiraan() {
        return namaPerkiraan;
    }

    public void setNamaPerkiraan(String namaPerkiraan) {
        this.namaPerkiraan = namaPerkiraan;
    }

    public double getAnggaran() {
        return anggaran;
    }

    public void setAnggaran(double anggaran) {
        this.anggaran = anggaran;
    }

    public double getDebetNeracaPeriodeLalu() {
        return debetNeracaPeriodeLalu;
    }

    public void setDebetNeracaPeriodeLalu(double debetNeracaPeriodeLalu) {
        this.debetNeracaPeriodeLalu = debetNeracaPeriodeLalu;
    }

    public double getKreditNeracaPeriodeLalu() {
        return kreditNeracaPeriodeLalu;
    }

    public void setKreditNeracaPeriodeLalu(double kreditNeracaPeriodeLalu) {
        this.kreditNeracaPeriodeLalu = kreditNeracaPeriodeLalu;
    }

    public double getDebetMutasi() {
        return debetMutasi;
    }

    public void setDebetMutasi(double debetMutasi) {
        this.debetMutasi = debetMutasi;
    }

    public double getKreditMutasi() {
        return kreditMutasi;
    }

    public void setKreditMutasi(double kreditMutasi) {
        this.kreditMutasi = kreditMutasi;
    }

    public double getDebetNeracaSaldo() {
        return debetNeracaSaldo;
    }

    public void setDebetNeracaSaldo(double debetNeracaSaldo) {
        this.debetNeracaSaldo = debetNeracaSaldo;
    }

    public double getKreditNeracaSaldo() {
        return kreditNeracaSaldo;
    }

    public void setKreditNeracaSaldo(double kreditNeracaSaldo) {
        this.kreditNeracaSaldo = kreditNeracaSaldo;
    }

    public double getDebetLabaRugi() {
        return debetLabaRugi;
    }

    public void setDebetLabaRugi(double debetLabaRugi) {
        this.debetLabaRugi = debetLabaRugi;
    }

    public double getKreditLabaRugi() {
        return kreditLabaRugi;
    }

    public void setKreditLabaRugi(double kreditLabaRugi) {
        this.kreditLabaRugi = kreditLabaRugi;
    }

    public double getDebetNeraca() {
        return debetNeraca;
    }

    public void setDebetNeraca(double debetNeraca) {
        this.debetNeraca = debetNeraca;
    }

    public double getKreditNeraca() {
        return kreditNeraca;
    }

    public void setKreditNeraca(double kreditNeraca) {
        this.kreditNeraca = kreditNeraca;
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
    
}
