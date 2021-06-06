/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

import com.dimata.qdep.entity.Entity;

public class KolektibilitasPembayaranDetails extends KolektibilitasPembayaran {

  private long kolektibilitasId = 0;
  private int tipeKreidt = 0;
  private int maxHariTunggakanPokok = 0;
  private int maxHariJumlahTunggakanBunga = 0;

  public long getKolektibilitasId() {
    return kolektibilitasId;
  }

  public void setKolektibilitasId(long kolektibilitasId) {
    this.kolektibilitasId = kolektibilitasId;
  }

  public int getTipeKreidt() {
    return tipeKreidt;
  }

  public void setTipeKreidt(int tipeKreidt) {
    this.tipeKreidt = tipeKreidt;
  }

  public Integer getMaxHariTunggakanPokok() {
    return this.maxHariTunggakanPokok;
  }

  public void setMaxHariTunggakanPokok(int maxHariTunggakanPokok) {
    this.maxHariTunggakanPokok = maxHariTunggakanPokok;
  }

  public Integer getMaxHariJumlahTunggakanBunga() {
    return this.maxHariJumlahTunggakanBunga;
  }

  public void setMaxHariJumlahTunggakanBunga(int maxHariJumlahTunggakanBunga) {
    this.maxHariJumlahTunggakanBunga = maxHariJumlahTunggakanBunga;
  }

}
