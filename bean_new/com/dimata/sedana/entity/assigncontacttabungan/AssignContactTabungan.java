/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.assigncontacttabungan;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;

public class AssignContactTabungan extends Entity {

//    private long assignTabunganId = 0;
    private long masterTabunganId = 0;
    private long contactId = 0;
    private String noTabungan = "";

    //tambahan untuk join
    private long simpananId = 0;
    private long jenisSimpananId = 0;

    public long getSimpananId() {
        return simpananId;
    }

    public void setSimpananId(long simpananId) {
        this.simpananId = simpananId;
    }

    public long getJenisSimpananId() {
        return jenisSimpananId;
    }

    public void setJenisSimpananId(long jenisSimpananId) {
        this.jenisSimpananId = jenisSimpananId;
    }

//    public long getAssignTabunganId() {
//        return assignTabunganId;
//    }
//
//    public void setAssignTabunganId(long assignTabunganId) {
//        this.assignTabunganId = assignTabunganId;
//    }

    public long getMasterTabunganId() {
        return masterTabunganId;
    }

    public void setMasterTabunganId(long masterTabunganId) {
        this.masterTabunganId = masterTabunganId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getNoTabungan() {
        return noTabungan;
    }

    public void setNoTabungan(String noTabungan) {
        this.noTabungan = noTabungan;
    }

}
