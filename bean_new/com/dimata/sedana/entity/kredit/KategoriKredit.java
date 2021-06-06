/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;

public class KategoriKredit extends Entity {

  private String kategoryKredit = "";
  private String keterangan = "";

  public String getKategoryKredit() {
    return kategoryKredit;
  }

  public void setKategoryKredit(String kategoryKredit) {
    this.kategoryKredit = kategoryKredit;
  }

  public String getKeterangan() {
    return keterangan;
  }

  public void setKeterangan(String keterangan) {
    this.keterangan = keterangan;
  }

}
