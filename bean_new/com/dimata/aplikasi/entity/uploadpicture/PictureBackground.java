/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.entity.uploadpicture;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author arin
 */
public class PictureBackground extends Entity {

    private String namaPicture;
    private String keterangan;
    private String uploadPicture;
    private long loginId;    
    private boolean changePicture;

    /**
     * @return the namaPicture
     */
    public String getNamaPicture() {
        if (namaPicture == null || namaPicture.length() == 0) {
            return "";
        }
        return namaPicture;
    }

    /**
     * @param namaPicture the namaPicture to set
     */
    public void setNamaPicture(String namaPicture) {
        this.namaPicture = namaPicture;
    }

    /**
     * @return the keterangan
     */
    public String getKeterangan() {
        if (keterangan == null || keterangan.length() == 0) {
            return "";
        }
        return keterangan;
    }

    /**
     * @param keterangan the keterangan to set
     */
    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    /**
     * @return the uploadPicture
     */
    public String getUploadPicture() {
        return uploadPicture;
    }

    /**
     * @param uploadPicture the uploadPicture to set
     */
    public void setUploadPicture(String uploadPicture) {
        this.uploadPicture = uploadPicture;
    }

    /**
     * @return the loginId
     */
    public long getLoginId() {
        return loginId;
    }

    /**
     * @param loginId the loginId to set
     */
    public void setLoginId(long loginId) {
        this.loginId = loginId;
    }

    /**
     * @return the changePicture
     */
    public boolean isChangePicture() {
        return changePicture;
    }

    /**
     * @param changePicture the changePicture to set
     */
    public void setChangePicture(boolean changePicture) {
        this.changePicture = changePicture;
    }

   
}