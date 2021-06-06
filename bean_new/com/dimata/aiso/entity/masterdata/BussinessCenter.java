/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 * @author dwi
 */
public class BussinessCenter extends Entity{

    private String bussCenterName = "";
    private long bussGroupId = 0;
    private long contactId = 0;
    private String bussCenterDesc = "";

    public String getBussCenterDesc() {
        return bussCenterDesc;
    }

    public void setBussCenterDesc(String bussCenterDesc) {
        this.bussCenterDesc = bussCenterDesc;
    }

    public String getBussCenterName() {
        return bussCenterName;
    }

    public void setBussCenterName(String bussCenterName) {
        this.bussCenterName = bussCenterName;
    }

    public long getBussGroupId() {
        return bussGroupId;
    }

    public void setBussGroupId(long bussGroupId) {
        this.bussGroupId = bussGroupId;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
    
    
}
