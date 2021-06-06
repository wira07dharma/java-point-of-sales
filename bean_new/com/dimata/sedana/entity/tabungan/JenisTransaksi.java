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
import com.dimata.sedana.common.I_Sedana;
import com.dimata.sedana.session.json.JSONObject;

public class JenisTransaksi extends Entity implements I_Sedana, Cloneable {
    
    private String jenisTransaksi = "";
    private long afliasiId = 0;
    private Integer tipeArusKas = 0;
    private Integer typeProsedur = 0;
    private Integer prosedureUntuk = 0;
    private Double prosentasePerhitungan = 0D;
    private Integer typeTransaksi = 0;
    private Double valueStandarTransaksi = 0D;
    private Integer statusAktif = 0;
    private Integer tipeDoc = 0;
    private Integer inputOption = 0;

    public Integer getInputOption() {
        return inputOption;
    }

    public void setInputOption(Integer inputOption) {
        this.inputOption = inputOption;
    }

    public Integer getTipeDoc() {
        return tipeDoc;
    }

    public void setTipeDoc(Integer tipeDoc) {
        this.tipeDoc = tipeDoc;
    }

    public Integer getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(Integer statusAktif) {
        this.statusAktif = statusAktif;
    }

    public Double getValueStandarTransaksi() {
        return valueStandarTransaksi;
    }

    public void setValueStandarTransaksi(Double valueStandarTransaksi) {
        this.valueStandarTransaksi = valueStandarTransaksi;
    }

    public Integer getTypeProsedur() {
        return typeProsedur;
    }

    public void setTypeProsedur(Integer typeProsedur) {
        this.typeProsedur = typeProsedur;
    }

    public Integer getProsedureUntuk() {
        return prosedureUntuk;
    }

    public void setProsedureUntuk(Integer prosedureUntuk) {
        this.prosedureUntuk = prosedureUntuk;
    }

    public Double getProsentasePerhitungan() {
        return prosentasePerhitungan;
    }

    public void setProsentasePerhitungan(Double prosentasePerhitungan) {
        this.prosentasePerhitungan = prosentasePerhitungan;
    }

    public Integer getTypeTransaksi() {
        return typeTransaksi;
    }

    public void setTypeTransaksi(Integer typeTransaksi) {
        this.typeTransaksi = typeTransaksi;
    }

    public long getAfliasiId() {
        return afliasiId;
    }

    public void setAfliasiId(long afliasiId) {
        this.afliasiId = afliasiId;
    }

    public Integer getTipeArusKas() {
        return tipeArusKas;
    }

    public void setTipeArusKas(Integer tipeArusKas) {
        this.tipeArusKas = tipeArusKas;
    }

    /**
     * @return the jenisTransaksi
     */
    public String getJenisTransaksi() {
        return jenisTransaksi;
    }

    /**
     * @param jenisTransaksi the jenisTransaksi to set
     */
    public void setJenisTransaksi(String jenisTransaksi) {
        this.jenisTransaksi = jenisTransaksi;
    }
    
    public JSONObject historyNew() {
    JSONObject j = new JSONObject();
    try {
      j.put("Jenis Transaksi", getJenisTransaksi());
      j.put("Afiliasi", getAfliasiId());
      j.put("Tipe Arus Kas", getTipeArusKas());
      j.put("Tipe Prosedur", getTypeProsedur());
      j.put("Prosedur Untuk", getProsedureUntuk());
      j.put("Prosentase Perhitungan", getProsentasePerhitungan());
      j.put("Tipe Transaksi", getTypeTransaksi());
      j.put("Nilai Standar Transaksi", getValueStandarTransaksi());
      j.put("Status Aktif", getStatusAktif());
      j.put("Tipe Dokumen", getTipeDoc());
      j.put("Input Option", getInputOption());
    } catch (Exception ex) {
      System.err.println(ex);
    }
    return j;
  }

  public JSONObject historyCompare(JenisTransaksi o) {
    JSONObject j = new JSONObject();
    try {
      if (getJenisTransaksi() != (o.getJenisTransaksi())) {
        j.put("Jenis Transaksi", "Dari " + getJenisTransaksi() + " menjadi " + getJenisTransaksi());
      }
      if (getAfliasiId() != (o.getAfliasiId())) {
        j.put("Afiliasi", "Dari " + getAfliasiId() + " menjadi " + o.getAfliasiId());
      }
      if (!getTipeArusKas().equals(o.getTipeArusKas())) {
        j.put("Tipe Arus Kas", "Dari " + getTipeArusKas() + " menjadi " + getTipeArusKas());
      }
      if (!getTypeProsedur().equals(o.getTypeProsedur())) {
        j.put("Tipe Prosedur", "Dari " + getTypeProsedur() + " menjadi " + getTypeProsedur());
      }
      if (!getProsedureUntuk().equals(o.getProsedureUntuk())) {
        j.put("Prosedur Untuk", "Dari " + getProsedureUntuk() + " menjadi " + getProsedureUntuk());
      }
      if (!getProsentasePerhitungan().equals(o.getProsentasePerhitungan())) {
        j.put("Tipe Transaksi", "Dari " + getProsentasePerhitungan() + " menjadi " + getProsentasePerhitungan());
      }
      if (!getTypeTransaksi().equals(o.getTypeTransaksi())) {
        j.put("Tipe Transaksi", "Dari " + getTypeTransaksi() + " menjadi " + getTypeTransaksi());
      }
      if (!getValueStandarTransaksi().equals(o.getValueStandarTransaksi())) {
        j.put("Nilai Standar Transaksi", "Dari " + getValueStandarTransaksi() + " menjadi " + getValueStandarTransaksi());
      }
      if (!getStatusAktif().equals(o.getStatusAktif())) {
        j.put("Status Aktif", "Dari " + getStatusAktif() + " menjadi " + getStatusAktif());
      }
      if (!getTipeDoc().equals(o.getTipeDoc())) {
        j.put("Tipe Dokumen", "Dari " + getTipeDoc() + " menjadi " + getTipeDoc());
      }
      if (!getInputOption().equals(o.getInputOption())) {
        j.put("Input Option", "Dari " + getInputOption() + " menjadi " + getInputOption());
      }
    } catch (Exception ex) {
      System.err.println(ex);
    }
    return j;
  }

  public JenisTransaksi clone() {
    Object o = null;
    try {
      o = super.clone();;
    } catch (CloneNotSupportedException ex) {
      System.err.println(ex);
    }
    return (JenisTransaksi) o;
  }

}
