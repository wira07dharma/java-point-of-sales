/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author HaddyPuutraa (PKL)
 * Create Kamis, 21 Pebruari 2013
 */
public class AnggotaEducation extends Entity{
    private long anggotaId;
    private long educationId;
    private String educationDetail="";

    public AnggotaEducation() {
    }

    /**
     * @return the anggotaId
     */
    public long getAnggotaId() {
        return anggotaId;
    }

    /**
     * @param anggotaId the anggotaId to set
     */
    public void setAnggotaId(long anggotaId) {
        this.anggotaId = anggotaId;
    }

    /**
     * @return the educationId
     */
    public long getEducationId() {
        return educationId;
    }

    /**
     * @param educationId the educationId to set
     */
    public void setEducationId(long educationId) {
        this.educationId = educationId;
    }

    /**
     * @return the educationDetail
     */
    public String getEducationDetail() {
        return educationDetail;
    }

    /**
     * @param educationDetail the educationDetail to set
     */
    public void setEducationDetail(String educationDetail) {
        if(educationDetail == null){
            educationDetail = "";
        }
        this.educationDetail = educationDetail;
    }
}
