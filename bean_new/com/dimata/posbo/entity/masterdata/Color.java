/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dewa
 */
public class Color extends Entity {

    private String colorCode = "";
    private String colorName = "";

    /**
     * @return the colorCode
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * @param colorCode the colorCode to set
     */
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    /**
     * @return the coloName
     */
    public String getColorName() {
        return colorName;
    }

    /**
     * @param coloName the coloName to set
     */
    public void setColorName(String coloName) {
        this.colorName = coloName;
    }

   

}
