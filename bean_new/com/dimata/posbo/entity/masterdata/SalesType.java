/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class SalesType extends Entity {
    private String name = "";
    private int typeSales=0;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the typeSales
     */
    public int getTypeSales() {
        return typeSales;
    }

    /**
     * @param typeSales the typeSales to set
     */
    public void setTypeSales(int typeSales) {
        this.typeSales = typeSales;
    }
    
    
    
}
