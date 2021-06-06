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
public class Kecamatan extends Entity {
    
    private long idNegara = 0;
    private long idPropinsi = 0;
    private long idKabupaten = 0;
    private String kdKecamatan = "";
    private String nmKecamatan = "";

    public long getIdKabupaten() {
        return idKabupaten;
    }

    public void setIdKabupaten(long idKabupaten) {
        this.idKabupaten = idKabupaten;
    }

    public String getKdKecamatan(){
		return kdKecamatan;
	}

	public void setKdKecamatan(String kdKecamatan){
		if ( kdKecamatan == null ) {
			kdKecamatan = "";
		}
		this.kdKecamatan = kdKecamatan;
	}

	public String getNmKecamatan(){
		return nmKecamatan;
	}

	public void setNmKecamatan(String nmKecamatan){
		if ( nmKecamatan == null ) {
			nmKecamatan = "";
		}
		this.nmKecamatan = nmKecamatan;
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
    
}
