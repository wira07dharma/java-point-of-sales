/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class Accessories extends Entity {

    private String accessories_name = "";
    private String accessories_code = "";
    private int accessories_status = 0;

    public String getAccessories_name() {
        return accessories_name;
    }

    public void setAccessories_name(String accessories_name) {
        this.accessories_name = accessories_name;
    }

    public String getAccessories_code() {
        return accessories_code;
    }

    public void setAccessories_code(String accessories_code) {
        this.accessories_code = accessories_code;
    }

    /**
     * @return the accessories_status
     */
    public int getAccessories_status() {
        return accessories_status;
    }

    /**
     * @param accessories_status the accessories_status to set
     */
    public void setAccessories_status(int accessories_status) {
        this.accessories_status = accessories_status;
    }

}
