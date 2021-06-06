/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.sedana.entity.searchtabungan;

import com.dimata.qdep.entity.Entity;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Regen
 */
public class SearchTabungan extends Entity {
  private Date tanggalAwal = null;
  private Date tanggalAkhir = null;
  private String noRekening = "";
  private String nama = "";
  private Vector<Long> idJenisSimpanan = new Vector<Long>();
  
  public boolean isIdJenisSimpanan(long idjt) {
    for(long id: idJenisSimpanan) {
      if(id==idjt){
        return true;
      }
    }
    
    return false;
  }

  /**
   * @param idJenisSimpanan the idJenisSimpanan to set
   */
  public void setIdJenisSimpanan(String[] idJenisSimpanan) {
    for(String s: idJenisSimpanan) {
      this.idJenisSimpanan.add(Long.valueOf(s));
    }
  }

  /**
   * @return the idJenisTransaksi
   */
  public Vector<Long> getIdJenisSimpanan() {
    return idJenisSimpanan;
  }

  /**
   * @return the tanggalAwal
   */
  public Date getTanggalAwal() {
    return tanggalAwal;
  }

  /**
   * @param tanggalAwal the tanggalAwal to set
   */
  public void setTanggalAwal(Date tanggalAwal) {
    this.tanggalAwal = tanggalAwal;
  }

  /**
   * @return the tanggalAkhir
   */
  public Date getTanggalAkhir() {
    return tanggalAkhir;
  }

  /**
   * @param tanggalAkhir the tanggalAkhir to set
   */
  public void setTanggalAkhir(Date tanggalAkhir) {
    this.tanggalAkhir = tanggalAkhir;
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
}
