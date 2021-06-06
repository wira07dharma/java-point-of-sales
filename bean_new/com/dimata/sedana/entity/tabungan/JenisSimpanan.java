/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;

public class JenisSimpanan extends Entity {
    
    public static final int FREKUENSI_SIMPANAN_BEBAS = 0;
    public static final int FREKUENSI_SIMPANAN_SEKALI = 1;
    public static final int FREKUENSI_SIMPANAN_BERULANG = 2;
    
    public static final String[] FREKUENSI_SIMPANAN_TITLE = {"Sukarela/Bebas", "Pokok/Sekali", "Wajib/Bulanan"};
    
  private String namaSimpanan = "";
  private long setoranMin = 0;
  private long bunga = 0;
  private long provisi = 0;
  private long biayaAdmin = 0;
  private String descJenisSimpanan = "";
  private int frekuensiSimpanan = 0;
  private int frekuensiPenarikan = 0;

  public String getNamaSimpanan() {
    return namaSimpanan;
  }

  public void setNamaSimpanan(String namaSimpanan) {
    this.namaSimpanan = namaSimpanan;
  }

  public long getSetoranMin() {
    return setoranMin;
  }

  public void setSetoranMin(long setoranMin) {
    this.setoranMin = setoranMin;
  }

  public long getBunga() {
    return bunga;
  }

  public void setBunga(long bunga) {
    this.bunga = bunga;
  }

  public long getProvisi() {
    return provisi;
  }

  public void setProvisi(long provisi) {
    this.provisi = provisi;
  }

  public long getBiayaAdmin() {
    return biayaAdmin;
  }

  public void setBiayaAdmin(long biayaAdmin) {
    this.biayaAdmin = biayaAdmin;
  }

  public String getDescJenisSimpanan() {
    return descJenisSimpanan;
  }

  public void setDescJenisSimpanan(String descJenisSimpanan) {
    this.descJenisSimpanan = descJenisSimpanan;
  }

  public int getFrekuensiSimpanan() {
    return frekuensiSimpanan;
  }

  public void setFrekuensiSimpanan(int frekuensiSimpanan) {
    this.frekuensiSimpanan = frekuensiSimpanan;
  }

  public int getFrekuensiPenarikan() {
    return frekuensiPenarikan;
  }

  public void setFrekuensiPenarikan(int frekuensiPenarikan) {
    this.frekuensiPenarikan = frekuensiPenarikan;
  }

}
