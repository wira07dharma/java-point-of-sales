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
public class Province extends Entity{
    private String provinceName="";
    
    public Province(){     
    }
    
    public String getProvinceName () {
        return provinceName;
    }
    
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    
}
