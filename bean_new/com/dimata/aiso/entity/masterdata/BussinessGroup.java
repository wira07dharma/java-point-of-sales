/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dwi
 */
public class BussinessGroup extends Entity{

    private String bussGroupCode = "";
    private String bussGroupName = "";
    private String bussGroupAddress = "";
    private String bussGroupCity = "";
    private String bussGroupPhone = "";
    private String bussGroupFax = "";

    public String getBussGroupAddress() {
        return bussGroupAddress;
    }

    public void setBussGroupAddress(String bussGroupAddress) {
        this.bussGroupAddress = bussGroupAddress;
    }

    public String getBussGroupCity() {
        return bussGroupCity;
    }

    public void setBussGroupCity(String bussGroupCity) {
        this.bussGroupCity = bussGroupCity;
    }

    public String getBussGroupCode() {
        return bussGroupCode;
    }

    public void setBussGroupCode(String bussGroupCode) {
        this.bussGroupCode = bussGroupCode;
    }

    public String getBussGroupFax() {
        return bussGroupFax;
    }

    public void setBussGroupFax(String bussGroupFax) {
        this.bussGroupFax = bussGroupFax;
    }

    public String getBussGroupPhone() {
        return bussGroupPhone;
    }

    public void setBussGroupPhone(String bussGroupPhone) {
        this.bussGroupPhone = bussGroupPhone;
    }

    public String getBussGroupName() {
        return bussGroupName;
    }

    public void setBussGroupName(String bussGroupName) {
        this.bussGroupName = bussGroupName;
    }
    
    
}
