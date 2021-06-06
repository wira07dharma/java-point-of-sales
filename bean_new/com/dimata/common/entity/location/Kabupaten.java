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
public class Kabupaten extends Entity {
    
    
     private long idNegara = 0;
    private long idPropinsi = 0;
    private String kdKabupaten = "";
    private String nmKabupaten = "";

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

    /**
     * @return the idPropinsi
     */
    public long getIdPropinsi() {
        return idPropinsi;
    }

    /**
     * @param idPropinsi the idPropinsi to set
     */
    public void setIdPropinsi(long idPropinsi) {
        this.idPropinsi = idPropinsi;
    }

    /**
     * @return the kdKabupaten
     */
    public String getKdKabupaten() {
        return kdKabupaten;
    }

    /**
     * @param kdKabupaten the kdKabupaten to set
     */
    public void setKdKabupaten(String kdKabupaten) {
        this.kdKabupaten = kdKabupaten;
    }

    /**
     * @return the nmKabupaten
     */
    public String getNmKabupaten() {
        return nmKabupaten;
    }

    /**
     * @param nmKabupaten the nmKabupaten to set
     */
    public void setNmKabupaten(String nmKabupaten) {
        this.nmKabupaten = nmKabupaten;
    }
    
}
