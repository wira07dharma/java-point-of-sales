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

public class AssignContact extends Entity {

  private long masterTabunganId = 0;
  private long contactId = 0;
  private String noTabungan = "";

  public long getContactId() {
    return contactId;
  }

  public void setContactId(long contactId) {
    this.contactId = contactId;
  }

  public String getNoTabungan() {
    return noTabungan;
  }

  public void setNoTabungan(String noTabungan) {
    this.noTabungan = noTabungan;
  }

  /**
   * @return the masterTabunganId
   */
  public long getMasterTabunganId() {
    return masterTabunganId;
  }

  /**
   * @param masterTabunganId the masterTabunganId to set
   */
  public void setMasterTabunganId(long masterTabunganId) {
    this.masterTabunganId = masterTabunganId;
  }

}
