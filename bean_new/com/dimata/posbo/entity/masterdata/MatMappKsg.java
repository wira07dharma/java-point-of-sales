/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class MatMappKsg extends Entity {
    public long getMaterialId() {
        return this.getOID(1);
    }

    public void setMaterialId(long materialId) {
        this.setOID(1, materialId);
    }

    public long getKsgId() {
        return this.getOID(0);
    }

    public void setKsgId(long ksgId) {
        this.setOID(0, ksgId);
    }
}
