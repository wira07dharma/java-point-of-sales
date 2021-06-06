/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.search;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dwi
 */
public class SrcCompany extends Entity{

    private String code = "";
    private String name = "";
    private String town = "";
    private String province = "";
    private String country = "";
    private String address = "";
    private int orderBy = 0;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTown() {
        return this.town;
    }

    public void setTown(String town) {
        this.town = town;
    }
    
    
}
