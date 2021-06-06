/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.sedana.session;

import com.dimata.qdep.entity.Entity;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Regen
 */
public class Tabungan extends Entity {
  private Date tanggal = null;
  private String noTabungan = null;
  private String nama = null;
  private String alamat = null;
  private long assignContactTabunganId = 0;
  private Vector<List> simpanan = new Vector<List>();
  
  //ADDED BY DEWOK 20180827 FOR PENUTUPAN TABUNGAN
  private String alasanTutup = null;

  public class List extends Entity{
    private String namaSimpanan = "";
    private String namaTabungan = null;
    private long idJenisTransaksi = 0;
    private double saldoAwal = 0;
    private double inputSaldo = 0;
    
    /**
     * @return the namaTabungan
     */
    public String getNamaTabungan() {
      return namaTabungan;
    }

    /**
     * @param namaTabungan the namaTabungan to set
     */
    public void setNamaTabungan(String namaTabungan) {
      this.namaTabungan = namaTabungan;
    }

    /**
     * @return the idJenisTransaksi
     */
    public long getIdJenisTransaksi() {
      return idJenisTransaksi;
    }

    /**
     * @param idJenisTransaksi the idJenisTransaksi to set
     */
    public void setIdJenisTransaksi(long idJenisTransaksi) {
      this.idJenisTransaksi = idJenisTransaksi;
    }

    /**
     * @return the totalSaldo
     */
    public double getSaldoAwal() {
      return saldoAwal;
    }

    /**
     * @param totalSaldo the totalSaldo to set
     */
    public void setSaldoAwal(double totalSaldo) {
      this.saldoAwal = totalSaldo;
    }

    /**
     * @return the inputSaldo
     */
    public double getInputSaldo() {
      return inputSaldo;
    }

    /**
     * @param inputSaldo the inputSaldo to set
     */
    public void setInputSaldo(double inputSaldo) {
      this.inputSaldo = inputSaldo;
    }

    /**
     * @return the namaTabungan
     */
    public String getNamaSimpanan() {
      return namaSimpanan;
    }

    /**
     * @param namaTabungan the namaTabungan to set
     */
    public void setNamaSimpanan(String namaSimpanan) {
      this.namaSimpanan = namaSimpanan;
    }
  }

  /**
   * @return the tanggal
   */
  public Date getTanggal() {
    return tanggal;
  }

  /**
   * @param tanggal the tanggal to set
   */
  public void setTanggal(Date tanggal) {
    this.tanggal = tanggal;
  }

  /**
   * @return the noTabungan
   */
  public String getNoTabungan() {
    return noTabungan;
  }

  /**
   * @param noTabungan the noTabungan to set
   */
  public void setNoTabungan(String noTabungan) {
    this.noTabungan = noTabungan;
  }

  /**
   * @return the nama
   */
  public String getNama() {
    return nama;
  }

  /**
   * @param nama the nama to set
   */
  public void setNama(String nama) {
    this.nama = nama;
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

    public String getAlasanTutup() {
        return alasanTutup;
    }

    public void setAlasanTutup(String alasanTutup) {
        this.alasanTutup = alasanTutup;
    }
  
  public void addSimpanan(long oidSimpanan, String namaTabungan, String namaSimpanan, long idJenisTransaksi, double totalSaldo, double inputSaldo) {
    List l = new List();
    l.setOID(oidSimpanan);
    l.setNamaTabungan(namaTabungan);
    l.setNamaSimpanan(namaSimpanan);
    l.setIdJenisTransaksi(idJenisTransaksi);
    l.setSaldoAwal(totalSaldo);
    l.setInputSaldo(inputSaldo);
    simpanan.add(l);
  }
  
  public Vector<List> getSimpanan() {
    return simpanan;
  }
  
  public List getSimpanan(int i) {
    return simpanan.get(i);
  }
  
  public Vector<List> getVSimpanan() {
    return simpanan;
  }
  
  /**
   * @return the assignContactTabunganId
   */
  public long getAssignContactTabunganId() {
    return assignContactTabunganId;
  }

  /**
   * @param assignContactTabunganId the assignContactTabunganId to set
   */
  public void setAssignContactTabunganId(long assignContactTabunganId) {
    this.assignContactTabunganId = assignContactTabunganId;
  }

}
