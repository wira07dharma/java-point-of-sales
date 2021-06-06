/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author sangtel6
 */
public class MasterGroup extends Entity  {
    
    
     private int typeGroup;
     private String namaGroup = "";

    /**
     * @return the typeGroup
     */
    public int getTypeGroup() {
        return typeGroup;
    }

    /**
     * @param typeGroup the typeGroup to set
     */
    public void setTypeGroup(int typeGroup) {
        this.typeGroup = typeGroup;
    }

    /**
     * @return the namaGroup
     */
    public String getNamaGroup() {
        return namaGroup;
    }

    /**
     * @param namaGroup the namaGroup to set
     */
    public void setNamaGroup(String namaGroup) {
        this.namaGroup = namaGroup;
    }
    
}
