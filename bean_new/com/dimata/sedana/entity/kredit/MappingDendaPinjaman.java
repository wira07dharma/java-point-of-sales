/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class MappingDendaPinjaman extends Entity {

    private double nilaiDenda = 0;
    private int tipeDendaBerlaku = 0;
    private int tipePerhitunganDenda = 0;
    private int frekuensiDenda = 0;
    private int satuanFrekuensiDenda = 0;
    private long pinjamanId = 0;
    private int dendaToleransi = 0;
    private int variabelDenda = 0;
    //added by dewok 2018-09-05 untuk multi fungsi tipe angsuran (denda/bunga tambahan)
    private int jenisAngsuran = 0;
    //added by dewok 20181025
    private int tipeVariabelDenda = 0;
    private int tipeFrekuensiDenda = 0;

    public double getNilaiDenda() {
        return nilaiDenda;
    }

    public void setNilaiDenda(double nilaiDenda) {
        this.nilaiDenda = nilaiDenda;
    }

    public int getTipeDendaBerlaku() {
        return tipeDendaBerlaku;
    }

    public void setTipeDendaBerlaku(int tipeDendaBerlaku) {
        this.tipeDendaBerlaku = tipeDendaBerlaku;
    }

    public int getTipePerhitunganDenda() {
        return tipePerhitunganDenda;
    }

    public void setTipePerhitunganDenda(int tipePerhitunganDenda) {
        this.tipePerhitunganDenda = tipePerhitunganDenda;
    }

    public int getFrekuensiDenda() {
        return frekuensiDenda;
    }

    public void setFrekuensiDenda(int frekuensiDenda) {
        this.frekuensiDenda = frekuensiDenda;
    }

    public int getSatuanFrekuensiDenda() {
        return satuanFrekuensiDenda;
    }

    public void setSatuanFrekuensiDenda(int satuanFrekuensiDenda) {
        this.satuanFrekuensiDenda = satuanFrekuensiDenda;
    }

    public long getPinjamanId() {
        return pinjamanId;
    }

    public void setPinjamanId(long pinjamanId) {
        this.pinjamanId = pinjamanId;
    }

    public int getDendaToleransi() {
        return dendaToleransi;
    }

    public void setDendaToleransi(int dendaToleransi) {
        this.dendaToleransi = dendaToleransi;
    }

    public int getVariabelDenda() {
        return variabelDenda;
    }

    public void setVariabelDenda(int variabelDenda) {
        this.variabelDenda = variabelDenda;
    }

    public int getJenisAngsuran() {
        return jenisAngsuran;
    }

    public void setJenisAngsuran(int jenisAngsuran) {
        this.jenisAngsuran = jenisAngsuran;
    }

    public int getTipeVariabelDenda() {
        return tipeVariabelDenda;
    }

    public void setTipeVariabelDenda(int tipeVariabelDenda) {
        this.tipeVariabelDenda = tipeVariabelDenda;
    }

    public int getTipeFrekuensiDenda() {
        return tipeFrekuensiDenda;
    }

    public void setTipeFrekuensiDenda(int tipeFrekuensiDenda) {
        this.tipeFrekuensiDenda = tipeFrekuensiDenda;
    }

}
