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
public class ReportTabunganValue extends Entity {
  private String namaAnggota = null;
  private String kodeAnggota = null;
  private String alamat = null;
  private String tanggalLaporan = null;
  private String kodeRekening = null;
  private String jenisTabungan = null;
  private Vector<ReportRow> reports = new Vector<ReportRow>();
  public static class ReportRow {

    /**
     * @return the date
     */
    public Date getDate() {
      return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
      this.date = date;
    }

    /**
     * @return the debit
     */
    public long getDebit() {
      return debit;
    }

    /**
     * @param debit the debit to set
     */
    public void setDebit(long debit) {
      this.debit = debit;
    }

    /**
     * @return the kredit
     */
    public long getKredit() {
      return kredit;
    }

    /**
     * @param kredit the kredit to set
     */
    public void setKredit(long kredit) {
      this.kredit = kredit;
    }

    /**
     * @return the saldo
     */
    public long getSaldo() {
      return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(long saldo) {
      this.saldo = saldo;
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
    private Date date = null;
    private long debit = 0;
    private long kredit = 0;
    private long saldo = 0;
    private String keterangan = null;
  }

  /**
   * @return the namaAnggota
   */
  public String getNamaAnggota() {
    return namaAnggota;
  }

  /**
   * @param namaAnggota the namaAnggota to set
   */
  public void setNamaAnggota(String namaAnggota) {
    this.namaAnggota = namaAnggota;
  }

  /**
   * @return the kodeAnggota
   */
  public String getKodeAnggota() {
    return kodeAnggota;
  }

  /**
   * @param kodeAnggota the kodeAnggota to set
   */
  public void setKodeAnggota(String kodeAnggota) {
    this.kodeAnggota = kodeAnggota;
  }

  /**
   * @return the alamat
   */
  public String getAlamat() {
    return alamat;
  }

  /**
   * @param alamat the alamat to set
   */
  public void setAlamat(String alamat) {
    this.alamat = alamat;
  }

  /**
   * @return the tanggalLaporan
   */
  public String getTanggalLaporan() {
    return tanggalLaporan;
  }

  /**
   * @param tanggalLaporan the tanggalLaporan to set
   */
  public void setTanggalLaporan(String tanggalLaporan) {
    this.tanggalLaporan = tanggalLaporan;
  }

  /**
   * @return the kodeRekening
   */
  public String getKodeRekening() {
    return kodeRekening;
  }

  /**
   * @param kodeRekening the kodeRekening to set
   */
  public void setKodeRekening(String kodeRekening) {
    this.kodeRekening = kodeRekening;
  }

  /**
   * @return the jenisTabungan
   */
  public String getJenisTabungan() {
    return jenisTabungan;
  }

  /**
   * @param jenisTabungan the jenisTabungan to set
   */
  public void setJenisTabungan(String jenisTabungan) {
    this.jenisTabungan = jenisTabungan;
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
  public void setReports(Vector<ReportRow> reports) {
    this.reports = reports;
  }

  /**
   * @param reports the reports to set
   */
  public void addReport(ReportRow report) {
    this.reports.add(report);
  }
}
