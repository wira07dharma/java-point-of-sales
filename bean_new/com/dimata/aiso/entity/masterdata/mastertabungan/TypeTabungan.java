/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.mastertabungan;
import com.dimata.qdep.entity.*;
/**
 *
 * @author Dede
 */
public class TypeTabungan extends Entity{
    
    private String TypeTabungan = "";
    private long afliasiId=0;
    /**
     * @return the TypeTabungan
     */
    public String getTypeTabungan() {
        return TypeTabungan;
    }

    /**
     * @param TypeTabungan the TypeTabungan to set
     */
    public void setTypeTabungan(String TypeTabungan) {
        this.TypeTabungan = TypeTabungan;
    }

    /**
     * @return the afliasiId
     */
    public long getAfliasiId() {
        return afliasiId;
    }

    /**
     * @param afliasiId the afliasiId to set
     */
    public void setAfliasiId(long afliasiId) {
        this.afliasiId = afliasiId;
    }
    
    
    
}
