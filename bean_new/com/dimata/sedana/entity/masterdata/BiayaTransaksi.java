/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class BiayaTransaksi extends Entity {

    private long idJenisTransaksi = 0;
    private long idPinjaman = 0;
    private double valueBiaya = 0;
    private int tipeBiaya = 0;
    private long idValue = 0;

    //tambahan non database
    private int tipeDoc = 0;
    private int inputOption = 0;

    //nilai konstanta
    public static final int TIPE_BIAYA_PERSENTASE = 0;
    public static final int TIPE_BIAYA_UANG = 1;

    public int getTipeDoc() {
        return tipeDoc;
    }

    public void setTipeDoc(int tipeDoc) {
        this.tipeDoc = tipeDoc;
    }

    public int getInputOption() {
        return inputOption;
    }

    public void setInputOption(int inputOption) {
        this.inputOption = inputOption;
    }

    public int getTipeBiaya() {
        return tipeBiaya;
    }

    public void setTipeBiaya(int tipeBiaya) {
        this.tipeBiaya = tipeBiaya;
    }

    public long getIdJenisTransaksi() {
        return idJenisTransaksi;
    }

    public void setIdJenisTransaksi(long idJenisTransaksi) {
        this.idJenisTransaksi = idJenisTransaksi;
    }

    public long getIdPinjaman() {
        return idPinjaman;
    }

    public void setIdPinjaman(long idPinjaman) {
        this.idPinjaman = idPinjaman;
    }

    public double getValueBiaya() {
        return valueBiaya;
    }

    public void setValueBiaya(double valueBiaya) {
        this.valueBiaya = valueBiaya;
    }

  /**
   * @return the idValue
   */
  public long getIdValue() {
    return idValue;
  }

  /**
   * @param idValue the idValue to set
   */
  public void setIdValue(long idValue) {
    this.idValue = idValue;
  }

}
