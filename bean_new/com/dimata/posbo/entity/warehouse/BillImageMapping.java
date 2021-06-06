/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;

public class BillImageMapping extends Entity {

  private long billMainId = 0;
  private String fileName = "";
  private String documentName = "";
  private String keterangan = "";

  public long getBillMainId() {
    return billMainId;
  }

  public void setBillMainId(long billMainId) {
    this.billMainId = billMainId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getDocumentName() {
    return documentName;
  }

  public void setDocumentName(String documentName) {
    this.documentName = documentName;
  }

  public String getKeterangan() {
    return keterangan;
  }

  public void setKeterangan(String keterangan) {
    this.keterangan = keterangan;
  }

}
