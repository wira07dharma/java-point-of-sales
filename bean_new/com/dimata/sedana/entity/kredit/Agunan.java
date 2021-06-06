/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

import com.dimata.qdep.entity.Entity;
import java.util.HashMap;

/**
 *
 * @author Dimata 007
 */
public class Agunan extends Entity {

    private String kodeJenisAgunan = "";
    private long contactId = 0;
    private long kodeKabLokasiAgunan = 0;
    private String alamatAgunan = "";
    private double nilaiAgunanNjop = 0;
    private String buktiKepemilikan = "";
    private int tipeAgunan = 0;
    private String noteTipeAgunan = "";
    private double nilaiEkonomis = 0;
    private double nilaiJaminanAgunan = 0;
    
    public static final HashMap<Integer, HashMap<String, String>> TIPE_AGUNAN = new HashMap<Integer, HashMap<String, String>>() {{
      put(1, new HashMap<String, String>(){{ put("TITLE", "Tanah"); put("NOTE_NAME", "Sertifikat Tanah"); }});
      put(2, new HashMap<String, String>(){{ put("TITLE", "Bangunan"); put("NOTE_NAME", "Sertifikat"); }});
      put(3, new HashMap<String, String>(){{ put("TITLE", "Kendaraan bermotor"); put("NOTE_NAME", "BPKB"); }});
      put(4, new HashMap<String, String>(){{ put("TITLE", "Mesin-mesin pabrik"); put("NOTE_NAME", "Lainnya"); }});
      put(5, new HashMap<String, String>(){{ put("TITLE", "Surat berharga dan saham"); put("NOTE_NAME", "Lainnya"); }});
      put(6, new HashMap<String, String>(){{ put("TITLE", "Pesawat udara atau kapal laut"); put("NOTE_NAME", "Lainnya"); }});
    }};

    private long agunanMappingId = 0;

    public long getAgunanMappingId() {
        return agunanMappingId;
    }

    public void setAgunanMappingId(long agunanMappingId) {
        this.agunanMappingId = agunanMappingId;
    }

    public String getKodeJenisAgunan() {
        return kodeJenisAgunan;
    }

    public void setKodeJenisAgunan(String kodeJenisAgunan) {
        this.kodeJenisAgunan = kodeJenisAgunan;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getKodeKabLokasiAgunan() {
        return kodeKabLokasiAgunan;
    }

    public void setKodeKabLokasiAgunan(long kodeKabLokasiAgunan) {
        this.kodeKabLokasiAgunan = kodeKabLokasiAgunan;
    }

    public String getAlamatAgunan() {
        return alamatAgunan;
    }

    public void setAlamatAgunan(String alamatAgunan) {
        this.alamatAgunan = alamatAgunan;
    }

    public double getNilaiAgunanNjop() {
        return nilaiAgunanNjop;
    }

    public void setNilaiAgunanNjop(double nilaiAgunanNjop) {
        this.nilaiAgunanNjop = nilaiAgunanNjop;
    }

    public String getBuktiKepemilikan() {
        return buktiKepemilikan;
    }

    public void setBuktiKepemilikan(String buktiKepemilikan) {
        this.buktiKepemilikan = buktiKepemilikan;
    }

  /**
   * @return the tipeAgunan
   */
  public int getTipeAgunan() {
    return tipeAgunan;
  }

  /**
   * @param tipeAgunan the tipeAgunan to set
   */
  public void setTipeAgunan(int tipeAgunan) {
    this.tipeAgunan = tipeAgunan;
  }

  /**
   * @return the noteTipeAgunan
   */
  public String getNoteTipeAgunan() {
    return noteTipeAgunan;
  }

  /**
   * @param noteTipeAgunan the noteTipeAgunan to set
   */
  public void setNoteTipeAgunan(String noteTipeAgunan) {
    this.noteTipeAgunan = noteTipeAgunan;
  }

    /**
     * @return the nilaiEkonomis
     */
    public double getNilaiEkonomis() {
        return nilaiEkonomis;
    }

    /**
     * @param nilaiEkonomis the nilaiEkonomis to set
     */
    public void setNilaiEkonomis(double nilaiEkonomis) {
        this.nilaiEkonomis = nilaiEkonomis;
    }

    /**
     * @return the nilaiJaminanAgunan
     */
    public double getNilaiJaminanAgunan() {
        return nilaiJaminanAgunan;
    }

    /**
     * @param nilaiJaminanAgunan the nilaiJaminanAgunan to set
     */
    public void setNilaiJaminanAgunan(double nilaiJaminanAgunan) {
        this.nilaiJaminanAgunan = nilaiJaminanAgunan;
    }

}
