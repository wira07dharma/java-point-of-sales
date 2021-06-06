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
public class Regency extends Entity{
    private String regencyName="";
    
    //UPDATE tanggal 7 Maret 2013 oleh Hadi
    private long idProvince;
    
    
    
    public Regency(){     
    }
    
    public String getRegencyName () {
        return regencyName;
    }
    
    public void setRegencyName(String regencyName) {
        this.regencyName = regencyName;
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
    
}
