/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.reportsearch;

import com.dimata.qdep.entity.Entity;
import java.sql.Date;
import java.util.Vector;

/**
 *
 * @author Adi
 */
public class ReportRangkumanKolektabilitas extends Entity {

  private String jenisKredit = null;
  private String kelompok = null;
  private String sumberDana = null;
  private String dateRange = null;
  private String totalText = "TOTAL";
  private String totalKreditPokok = "0";
  private String totalKreditBunga = "0";
  private String totalJumlahNasabah = "0";
  private String totalJumlahKredit = "0";
  private String totalTunggakanPokok = "0";
  private String totalTunggakanBunga = "0";
  private String totalTunggakanTotal = "0";
  private Vector<ReportRow> reports = new Vector<ReportRow>();

  public static class ReportRow {

    private String kode = null;
    private String persentase = null;
    private String jmlNasabah = null;
    private String jmlKredit = null;
    private String kreditPokok = null;
    private String kreditBunga = null;
    private String tuggakanPokok = null;
    private String tunggakanBunga = null;
    private String tunggakanTotal = null;

    /**
     * @return the kode
     */
    public String getKode() {
      return kode;
    }

    /**
     * @param kode the kode to set
     */
    public void setKode(String kode) {
      this.kode = kode;
    }

    /**
     * @return the persentase
     */
    public String getPersentase() {
      return persentase;
    }

    /**
     * @param persentase the persentase to set
     */
    public void setPersentase(String persentase) {
      this.persentase = persentase;
    }

    /**
     * @return the jmlNasabah
     */
    public String getJmlNasabah() {
      return jmlNasabah;
    }

    /**
     * @param jmlNasabah the jmlNasabah to set
     */
    public void setJmlNasabah(String jmlNasabah) {
      this.jmlNasabah = jmlNasabah;
    }

    /**
     * @return the jmlKredit
     */
    public String getJmlKredit() {
      return jmlKredit;
    }

    /**
     * @param jmlKredit the jmlKredit to set
     */
    public void setJmlKredit(String jmlKredit) {
      this.jmlKredit = jmlKredit;
    }

    /**
     * @return the tunggakanTotal
     */
    public String getTunggakanTotal() {
      return tunggakanTotal;
    }

    /**
     * @param tunggakanTotal the tunggakanTotal to set
     */
    public void setTunggakanTotal(String tunggakanTotal) {
      this.tunggakanTotal = tunggakanTotal;
    }

    /**
     * @return the kreditPokok
     */
    public String getKreditPokok() {
      return kreditPokok;
    }

    /**
     * @param kreditPokok the kreditPokok to set
     */
    public void setKreditPokok(String kreditPokok) {
      this.kreditPokok = kreditPokok;
    }

    /**
     * @return the kreditBunga
     */
    public String getKreditBunga() {
      return kreditBunga;
    }

    /**
     * @param kreditBunga the kreditBunga to set
     */
    public void setKreditBunga(String kreditBunga) {
      this.kreditBunga = kreditBunga;
    }

    /**
     * @return the tuggakanPokok
     */
    public String getTuggakanPokok() {
      return tuggakanPokok;
    }

    /**
     * @param tuggakanPokok the tuggakanPokok to set
     */
    public void setTuggakanPokok(String tuggakanPokok) {
      this.tuggakanPokok = tuggakanPokok;
    }

    /**
     * @return the tunggakanBunga
     */
    public String getTunggakanBunga() {
      return tunggakanBunga;
    }

    /**
     * @param tunggakanBunga the tunggakanBunga to set
     */
    public void setTunggakanBunga(String tunggakanBunga) {
      this.tunggakanBunga = tunggakanBunga;
    }
  }
  
  /**
   * @return the totalJumlahNasabah
   */
  public String getTotalJumlahNasabah() {
    return totalJumlahNasabah;
  }

  /**
   * @param totalJumlahNasabah the totalJumlahNasabah to set
   */
  public void setTotalJumlahNasabah(String totalJumlahNasabah) {
    this.totalJumlahNasabah = totalJumlahNasabah;
  }

  /**
   * @return the totalJumlahKredit
   */
  public String getTotalJumlahKredit() {
    return totalJumlahKredit;
  }

  /**
   * @param totalJumlahKredit the totalJumlahKredit to set
   */
  public void setTotalJumlahKredit(String totalJumlahKredit) {
    this.totalJumlahKredit = totalJumlahKredit;
  }

  /**
   * @return the reports
   */
  public Vector<ReportRow> getReports() {
    return reports;
  }

  /**
   * @param reports the reports to set
   */
  public void addReports(ReportRow reportRow) {
    this.reports.add(reportRow);
  }
  
  /**
   * @return the jenisKredit
   */
  public String getJenisKredit() {
    return jenisKredit;
  }

  /**
   * @param jenisKredit the jenisKredit to set
   */
  public void setJenisKredit(String jenisKredit) {
    this.jenisKredit = jenisKredit;
  }

  /**
   * @return the kelompok
   */
  public String getKelompok() {
    return kelompok;
  }

  /**
   * @param kelompok the kelompok to set
   */
  public void setKelompok(String kelompok) {
    this.kelompok = kelompok;
  }

  /**
   * @return the sumberDana
   */
  public String getSumberDana() {
    return sumberDana;
  }

  /**
   * @param sumberDana the sumberDana to set
   */
  public void setSumberDana(String sumberDana) {
    this.sumberDana = sumberDana;
  }

  /**
   * @return the dateRange
   */
  public String getDateRange() {
    return dateRange;
  }

  /**
   * @param dateRange the dateRange to set
   */
  public void setDateRange(String dateRange) {
    this.dateRange = dateRange;
  }

  /**
   * @return the totalText
   */
  public String getTotalText() {
    return totalText;
  }

  /**
   * @param totalText the totalText to set
   */
  public void setTotalText(String totalText) {
    this.totalText = totalText;
  }

  /**
   * @return the totalKreditPokok
   */
  public String getTotalKreditPokok() {
    return totalKreditPokok;
  }

  /**
   * @param totalKreditPokok the totalKreditPokok to set
   */
  public void setTotalKreditPokok(String totalKreditPokok) {
    this.totalKreditPokok = totalKreditPokok;
  }

  /**
   * @return the totalKreditBunga
   */
  public String getTotalKreditBunga() {
    return totalKreditBunga;
  }

  /**
   * @param totalKreditBunga the totalKreditBunga to set
   */
  public void setTotalKreditBunga(String totalKreditBunga) {
    this.totalKreditBunga = totalKreditBunga;
  }

  /**
   * @return the totalTunggakanPokok
   */
  public String getTotalTunggakanPokok() {
    return totalTunggakanPokok;
  }

  /**
   * @param totalTunggakanPokok the totalTunggakanPokok to set
   */
  public void setTotalTunggakanPokok(String totalTunggakanPokok) {
    this.totalTunggakanPokok = totalTunggakanPokok;
  }

  /**
   * @return the totalTunggakanBunga
   */
  public String getTotalTunggakanBunga() {
    return totalTunggakanBunga;
  }

  /**
   * @param totalTunggakanBunga the totalTunggakanBunga to set
   */
  public void setTotalTunggakanBunga(String totalTunggakanBunga) {
    this.totalTunggakanBunga = totalTunggakanBunga;
  }

  /**
   * @return the totalTunggakanTotal
   */
  public String getTotalTunggakanTotal() {
    return totalTunggakanTotal;
  }

  /**
   * @param totalTunggakanTotal the totalTunggakanTotal to set
   */
  public void setTotalTunggakanTotal(String totalTunggakanTotal) {
    this.totalTunggakanTotal = totalTunggakanTotal;
  }

}
