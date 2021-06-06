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
public class Penjamin extends Entity {

    private long contactId = 0;
    private long pinjamanId = 0;
    private double prosentasePenjamin = 0;
    private long jenisTransaksiId = 0;
    private double coverage = 0;

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }        

    public long getJenisTransaksiId() {
        return jenisTransaksiId;
    }

    public void setJenisTransaksiId(long jenisTransaksiId) {
        this.jenisTransaksiId = jenisTransaksiId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getPinjamanId() {
        return pinjamanId;
    }

    public void setPinjamanId(long pinjamanId) {
        this.pinjamanId = pinjamanId;
    }

    public double getProsentasePenjamin() {
        return prosentasePenjamin;
    }

    public void setProsentasePenjamin(double prosentasePenjamin) {
        this.prosentasePenjamin = prosentasePenjamin;
    }

}
