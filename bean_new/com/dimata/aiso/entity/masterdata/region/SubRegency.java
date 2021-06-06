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
public class SubRegency extends Entity{
    private String subRegencyName="";
    
    //Update tanggal 7 Maret 2013 oleh Hadi
    private long idRegency;
    
    
    public SubRegency(){     
    }
    
    public String getSubRegencyName () {
        return subRegencyName;
    }
    
    public void setSubRegencyName(String subRegencyName) {
        this.subRegencyName = subRegencyName;
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
    
}
