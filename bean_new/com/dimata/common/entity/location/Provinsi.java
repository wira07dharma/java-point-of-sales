/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.location;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Acer
 */
public class Provinsi extends Entity  {
    
    private String kdProvinsi = "";
	private String nmProvinsi = "";
        private long idNegara = 0;

    /**
     * @return the kdProvinsi
     */
    public String getKdProvinsi() {
        return kdProvinsi;
    }

    /**
     * @param kdProvinsi the kdProvinsi to set
     */
    public void setKdProvinsi(String kdProvinsi) {
        this.kdProvinsi = kdProvinsi;
    }

    /**
     * @return the nmProvinsi
     */
    public String getNmProvinsi() {
        return nmProvinsi;
    }

    /**
     * @param nmProvinsi the nmProvinsi to set
     */
    public void setNmProvinsi(String nmProvinsi) {
        this.nmProvinsi = nmProvinsi;
    }

    /**
     * @return the idNegara
     */
    public long getIdNegara() {
        return idNegara;
    }

    /**
     * @param idNegara the idNegara to set
     */
    public void setIdNegara(long idNegara) {
        this.idNegara = idNegara;
    }
}
