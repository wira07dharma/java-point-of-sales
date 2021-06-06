/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class MaterialDetail extends Entity {

    private long materialId = 0;
    private double qty = 0;
    private double berat = 0;
    private double hargaBeli = 0;
    private double hargaJual = 0;
    private double rate = 0;
    private double faktorJual = 0;
    private double uphetPersentase = 0;
    private double uphetValue = 0;
    private double uphetPersentaseTot = 0;
    private double uphetValueTot = 0;
    private double ongkos = 0;

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public double getHargaBeli() {
        return hargaBeli;
    }

    public void setHargaBeli(double hargaBeli) {
        this.hargaBeli = hargaBeli;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getFaktorJual() {
        return faktorJual;
    }

    public void setFaktorJual(double faktorJual) {
        this.faktorJual = faktorJual;
    }

    public double getUphetPersentase() {
        return uphetPersentase;
    }

    public void setUphetPersentase(double uphetPersentase) {
        this.uphetPersentase = uphetPersentase;
    }

    public double getUphetValue() {
        return uphetValue;
    }

    public void setUphetValue(double uphetValue) {
        this.uphetValue = uphetValue;
    }

    public double getUphetPersentaseTot() {
        return uphetPersentaseTot;
    }

    public void setUphetPersentaseTot(double uphetPersentaseTot) {
        this.uphetPersentaseTot = uphetPersentaseTot;
    }

    public double getUphetValueTot() {
        return uphetValueTot;
    }

    public void setUphetValueTot(double uphetValueTot) {
        this.uphetValueTot = uphetValueTot;
    }

    public double getOngkos() {
        return ongkos;
    }

    public void setOngkos(double ongkos) {
        this.ongkos = ongkos;
    }

    public String getLogDetail(Entity prevDoc) {
        String history = "";
        MaterialDetail prevMatDet = (MaterialDetail) prevDoc;

        if (prevMatDet == null || prevMatDet.getOID() == 0) {
            history += "Qty : " + String.format("%,.0f", this.getQty()) + "; "
                    + "Berat : " + String.format("%,.3f", this.getBerat()) + "; "
                    + "Harga Beli : " + String.format("%,.0f", this.getHargaBeli()) + ".00; "
                    + "Harga Jual : " + String.format("%,.0f", this.getHargaJual()) + ".00; "
                    + "Rate : " + String.format("%,.0f", this.getRate()) + "; "
                    + "Faktor Jual : " + String.format("%,.2f", this.getFaktorJual()) + "; "
                    + "UP HET % : " + String.format("%,.2f", this.getUphetPersentase()) + "; "
                    + "UP HET Rp : " + String.format("%,.0f", this.getUphetValue()) + ".00; "
                    + "Total UP HET % : " + String.format("%,.2f", this.getUphetPersentaseTot()) + "; "
                    + "Total UP HET Rp : " + String.format("%,.0f", this.getUphetValueTot()) + ".00; "
                    + "Ongkos : " + String.format("%,.0f", this.getOngkos()) + ".00; "
                    + "";
        } else {
            if (prevMatDet.getQty() != this.getQty()) {
                history += "Qty changed from " + String.format("%,.0f", prevMatDet.getQty())
                        + " to " + String.format("%,.0f", this.getQty()) + "; ";
            }
            if (prevMatDet.getBerat() != this.getBerat()) {
                history += "Berat changed from " + String.format("%,.3f", prevMatDet.getBerat())
                        + " to " + String.format("%,.3f", this.getBerat()) + "; ";
            }
            if (prevMatDet.getHargaBeli() != this.getHargaBeli()) {
                history += "Harga Beli changed from " + String.format("%,.0f", prevMatDet.getHargaBeli()) + ".00"
                        + " to " + String.format("%,.0f", this.getHargaBeli()) + ".00; ";
            }
            if (prevMatDet.getHargaJual() != this.getHargaJual()) {
                history += "Harga Jual changed from " + String.format("%,.0f", prevMatDet.getHargaJual()) + ".00"
                        + " to " + String.format("%,.0f", this.getHargaJual()) + ".00; ";
            }
            if (prevMatDet.getRate() != this.getRate()) {
                history += "Rate changed from " + String.format("%,.0f", prevMatDet.getRate())
                        + " to " + String.format("%,.0f", this.getRate()) + "; ";
            }
            if (prevMatDet.getFaktorJual() != this.getFaktorJual()) {
                history += "Faktor Jual changed from " + String.format("%,.2f", prevMatDet.getFaktorJual())
                        + " to " + String.format("%,.2f", this.getFaktorJual()) + "; ";
            }
            if (prevMatDet.getUphetPersentase() != this.getUphetPersentase()) {
                history += "UP HET % changed from " + String.format("%,.2f", prevMatDet.getUphetPersentase())
                        + " to " + String.format("%,.2f", this.getUphetPersentase()) + "; ";
            }
            if (prevMatDet.getUphetValue() != this.getUphetValue()) {
                history += "UP HET Rp changed from " + String.format("%,.0f", prevMatDet.getUphetValue()) + ".00"
                        + " to " + String.format("%,.0f", this.getUphetValue()) + ".00; ";
            }
            if (prevMatDet.getUphetPersentaseTot() != this.getUphetPersentaseTot()) {
                history += "Total UP HET % changed from " + String.format("%,.2f", prevMatDet.getUphetPersentaseTot())
                        + " to " + String.format("%,.2f", this.getUphetPersentaseTot()) + "; ";
            }
            if (prevMatDet.getUphetValueTot() != this.getUphetValueTot()) {
                history += "Total UP HET Rp changed from " + String.format("%,.0f", prevMatDet.getUphetValueTot()) + ".00"
                        + " to " + String.format("%,.0f", this.getUphetValueTot()) + ".00; ";
            }
            if (prevMatDet.getOngkos() != this.getOngkos()) {
                history += "Ongkos changed from " + String.format("%,.0f", prevMatDet.getOngkos()) + ".00"
                        + " to " + String.format("%,.0f", this.getOngkos()) + ".00; ";
            }
        }
        return history;
    }

}
