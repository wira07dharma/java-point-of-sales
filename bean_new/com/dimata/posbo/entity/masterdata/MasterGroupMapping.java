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
public class MasterGroupMapping extends Entity {

    private long groupId = 0;
    private int modul = 0;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public int getModul() {
        return modul;
    }

    public void setModul(int modul) {
        this.modul = modul;
    }    
    
}
