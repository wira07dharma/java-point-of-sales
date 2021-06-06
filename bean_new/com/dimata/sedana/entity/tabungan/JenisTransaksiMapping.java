/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class JenisTransaksiMapping extends Entity {

    private long jenisTransaksiId = 0;
    private long idJenisSimpanan = 0;

    public long getJenisTransaksiId() {
        return jenisTransaksiId;
    }

    public void setJenisTransaksiId(long jenisTransaksiId) {
        this.jenisTransaksiId = jenisTransaksiId;
    }

    public long getIdJenisSimpanan() {
        return idJenisSimpanan;
    }

    public void setIdJenisSimpanan(long idJenisSimpanan) {
        this.idJenisSimpanan = idJenisSimpanan;
    }

}
