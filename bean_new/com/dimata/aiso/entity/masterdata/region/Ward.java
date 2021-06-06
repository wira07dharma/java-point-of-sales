/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.region;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dw1p4
 */
public class Ward extends Entity{
    private String wardName="";
    
    //FK Update tanggal 7 Maret 2013 oleh Hadi Putra
    private long idProvince;
    private long idCity;
    private long idRegency;
    private long idSubRegency;
    
    public Ward(){     
    }
    
    public String getWardName () {
        return wardName;
    }
    
    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    /**
     * @return the idProvince
     */
    public long getIdProvince() {
        return idProvince;
    }

    /**
     * @param idProvince the idProvince to set
     */
    public void setIdProvince(long idProvince) {
        this.idProvince = idProvince;
    }

    /**
     * @return the idCity
     */
    public long getIdCity() {
        return idCity;
    }

    /**
     * @param idCity the idCity to set
     */
    public void setIdCity(long idCity) {
        this.idCity = idCity;
    }

    /**
     * @return the idRegency
     */
    public long getIdRegency() {
        return idRegency;
    }

    /**
     * @param idRegency the idRegency to set
     */
    public void setIdRegency(long idRegency) {
        this.idRegency = idRegency;
    }

    /**
     * @return the idSubRegency
     */
    public long getIdSubRegency() {
        return idSubRegency;
    }

    /**
     * @param idSubRegency the idSubRegency to set
     */
    public void setIdSubRegency(long idSubRegency) {
        this.idSubRegency = idSubRegency;
    }
    
}
