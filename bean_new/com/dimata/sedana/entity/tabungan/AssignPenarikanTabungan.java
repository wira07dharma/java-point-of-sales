/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class AssignPenarikanTabungan extends Entity {

    private long masterTabunganId = 0;
    private long idTabunganRangePenarikan = 0;

    public long getMasterTabunganId() {
        return masterTabunganId;
    }

    public void setMasterTabunganId(long masterTabunganId) {
        this.masterTabunganId = masterTabunganId;
    }

    public long getIdTabunganRangePenarikan() {
        return idTabunganRangePenarikan;
    }

    public void setIdTabunganRangePenarikan(long idTabunganRangePenarikan) {
        this.idTabunganRangePenarikan = idTabunganRangePenarikan;
    }

}
