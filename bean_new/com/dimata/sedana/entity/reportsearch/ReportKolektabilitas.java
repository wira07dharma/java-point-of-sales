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
public class ReportKolektabilitas extends Entity {

  private String jenisKredit = null;
  private String kelompok = null;
  private String sumberDana = null;
  private String dateRange = null;
  private String totalText = "TOTAL";
  private String totalKreditPokok = "0";
  private String totalKreditBunga = "0";
  private String totalTunggakanPokok = "0";
  private String totalTunggakanBunga = "0";
  private String totalTunggakanTotal = "0";
  private Vector<ReportRow> reports = new Vector<ReportRow>();
  public static class ReportRow {
    private String kolektibilitas = null;
    private String noRekening = null;
    private String namaNasabah = null;
    private String jangkaWaktu = null;
    private String hariTunggakanPokok = null;
    private String hariTunggakanBunga = null;
    private String kreditPokok = null;
    private String kreditBunga = null;
    private String tuggakanPokok = null;
    private String tunggakanBunga = null;
    private String total = null;
    
    /**
     * @return the kolektibilitas
     */
    public String getKolektibilitas() {
      return kolektibilitas;
    }

    /**
     * @param kolektibilitas the kolektibilitas to set
     */
    public void setKolektibilitas(String kolektibilitas) {
      this.kolektibilitas = kolektibilitas;
    }

    /**
     * @return the noRekening
     */
    public String getNoRekening() {
      return noRekening;
    }

    /**
     * @param noRekening the noRekening to set
     */
    public void setNoRekening(String noRekening) {
      this.noRekening = noRekening;
    }

    /**
     * @return the namaNasabah
     */
    public String getNamaNasabah() {
      return namaNasabah;
    }

    /**
     * @param namaNasabah the namaNasabah to set
     */
    public void setNamaNasabah(String namaNasabah) {
      this.namaNasabah = namaNasabah;
    }

    /**
     * @return the jangkaWaktu
     */
    public String getJangkaWaktu() {
      return jangkaWaktu;
    }

    /**
     * @param jangkaWaktu the jangkaWaktu to set
     */
    public void setJangkaWaktu(String jangkaWaktu) {
      this.jangkaWaktu = jangkaWaktu;
    }

    /**
     * @return the hariTunggakanPokok
     */
    public String getHariTunggakanPokok() {
      return hariTunggakanPokok;
    }

    /**
     * @param hariTunggakanPokok the hariTunggakanPokok to set
     */
    public void setHariTunggakanPokok(String hariTunggakanPokok) {
      this.hariTunggakanPokok = hariTunggakanPokok;
    }

    /**
     * @return the hariTunggakanBunga
     */
    public String getHariTunggakanBunga() {
      return hariTunggakanBunga;
    }

    /**
     * @param hariTunggakanBunga the hariTunggakanBunga to set
     */
    public void setHariTunggakanBunga(String hariTunggakanBunga) {
      this.hariTunggakanBunga = hariTunggakanBunga;
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

    /**
     * @return the total
     */
    public String getTotal() {
      return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(String total) {
      this.total = total;
    }
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
