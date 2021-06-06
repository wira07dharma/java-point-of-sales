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

public class Afiliasi extends Entity {

  private long feeKoperasi = 0;
  private String nameAfiliasi = "";
  private String alamatAfiliasi = "";

  public long getFeeKoperasi() {
    return feeKoperasi;
  }

  public void setFeeKoperasi(long feeKoperasi) {
    this.feeKoperasi = feeKoperasi;
  }

  public String getNameAfiliasi() {
    return nameAfiliasi;
  }

  public void setNameAfiliasi(String nameAfiliasi) {
    this.nameAfiliasi = nameAfiliasi;
  }

  public String getAlamatAfiliasi() {
    return alamatAfiliasi;
  }

  public void setAlamatAfiliasi(String alamatAfiliasi) {
    this.alamatAfiliasi = alamatAfiliasi;
  }

}
