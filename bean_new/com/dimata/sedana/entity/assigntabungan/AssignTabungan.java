/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.assigntabungan;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;

public class AssignTabungan extends Entity {

  private long masterTabungan = 0;
  private long idJenisSimpanan = 0;

  public long getAssignMasterTabunganId() {
    return this.getOID();
  }

  public void setAssignMasterTabunganId(long assignMasterTabunganId) {
    this.setOID(assignMasterTabunganId);
  }

  public long getMasterTabungan() {
    return masterTabungan;
  }

  public void setMasterTabungan(long masterTabungan) {
    this.masterTabungan = masterTabungan;
  }

  public long getIdJenisSimpanan() {
    return idJenisSimpanan;
  }

  public void setIdJenisSimpanan(long idJenisSimpanan) {
    this.idJenisSimpanan = idJenisSimpanan;
  }

}
