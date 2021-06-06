/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aplikasi.entity.picturecompany;
import com.dimata.qdep.entity.Entity;

/**
 *
 * @author user
 */
public class PictureCompany extends Entity {
    //private String pictureCompanyId;
    private String namaPicture; 

    /**
     * @return the namaPicture
     */
    public String getNamaPicture() {
        return namaPicture;
    }

    /**
     * @param namaPicture the namaPicture to set
     */
    public void setNamaPicture(String namaPicture) {
        this.namaPicture = namaPicture;
    }

    
}
