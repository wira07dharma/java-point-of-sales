/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import com.dimata.sedana.session.json.JSONObject;

public class KolektibilitasPembayaran extends Entity implements Cloneable {

  private String kodeKolektibilitas = "";
  private Integer tingkatKolektibilitas = 0;
  private String judulKolektibilitas = "";
  private Integer maxHariTunggakanPokok = 0;
  private Integer maxHariJumlahTunggakanBunga = 0;
  private Float tingkatResiko = 0F;

  public String getKodeKolektibilitas() {
    return kodeKolektibilitas;
  }

  public void setKodeKolektibilitas(String kodeKolektibilitas) {
    this.kodeKolektibilitas = kodeKolektibilitas;
  }

  public Integer getTingkatKolektibilitas() {
    return tingkatKolektibilitas;
  }

  public void setTingkatKolektibilitas(Integer tingkatKolektibilitas) {
    this.tingkatKolektibilitas = tingkatKolektibilitas;
  }

  public String getJudulKolektibilitas() {
    return judulKolektibilitas;
  }

  public void setJudulKolektibilitas(String judulKolektibilitas) {
    this.judulKolektibilitas = judulKolektibilitas;
  }

  public Integer getMaxHariTunggakanPokok() {
    return maxHariTunggakanPokok;
  }

  public void setMaxHariTunggakanPokok(Integer maxHariTunggakanPokok) {
    this.maxHariTunggakanPokok = maxHariTunggakanPokok;
  }

  public Integer getMaxHariJumlahTunggakanBunga() {
    return maxHariJumlahTunggakanBunga;
  }

  public void setMaxHariJumlahTunggakanBunga(Integer maxHariJumlahTunggakanBunga) {
    this.maxHariJumlahTunggakanBunga = maxHariJumlahTunggakanBunga;
  }

  public Float getTingkatResiko() {
    return tingkatResiko;
  }

  public void setTingkatResiko(Float tingkatResiko) {
    this.tingkatResiko = tingkatResiko;
  }
  
  public JSONObject historyNew() {
    JSONObject j = new JSONObject();
    j.put("Kode Kolektibilitas", getTingkatKolektibilitas());
    j.put("Tingkat Kolektibilitas", getTingkatKolektibilitas());
    j.put("Judul Kolektibilitas", getJudulKolektibilitas());
    j.put("Tingkat Resiko", getTingkatResiko());
    return j;
  }

  public JSONObject historyCompare(KolektibilitasPembayaran o) {
    JSONObject j = new JSONObject();
    if (!getKodeKolektibilitas().equals(o.getKodeKolektibilitas())) {
      j.put("Kode Kolektibilitas", "Dari " + getKodeKolektibilitas() + " menjadi " + o.getKodeKolektibilitas());
    }
    if (!getTingkatKolektibilitas().equals(o.getTingkatKolektibilitas())) {
      j.put("Tingkat Kolektibilitas", "Dari " + getTingkatKolektibilitas() + " menjadi " + o.getTingkatKolektibilitas());
    }
    if (!getJudulKolektibilitas().equals(o.getJudulKolektibilitas())) {
      j.put("Judul Kolektibilitas", "Dari " + getJudulKolektibilitas() + " menjadi " + o.getJudulKolektibilitas());
    }
    if (!getTingkatResiko().equals(o.getTingkatResiko())) {
      j.put("Nomor Loket", "Dari " + getTingkatResiko() + " menjadi " + o.getTingkatResiko());
    }
    return j;
  }

  public KolektibilitasPembayaran clone() {
    Object o = null;
    try {
      o = super.clone();;
    } catch (CloneNotSupportedException ex) {
      System.err.println(ex);
    }
    return (KolektibilitasPembayaran) o;
  }

}
