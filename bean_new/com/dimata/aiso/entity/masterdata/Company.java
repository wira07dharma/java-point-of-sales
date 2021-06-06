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
public class Company extends Entity{
    
    private String companyCode = "";
    private String companyName = "";
    private String personName = "";
    private String personLastName = "";
    private String bussAddress = "";
    private String town = "";
    private String province = "";
    private String country = "";
    private String phoneNr = "";
    private String mobilePh = "";
    private String fax = "";
    private String compEmail = "";
    private String postalCode = "";
    private String compImage = "";

    public String getBussAddress() {
        return bussAddress;
    }

    public void setBussAddress(String bussAddress) {
        this.bussAddress = bussAddress;
    }

    public String getCompEmail() {
        return compEmail;
    }

    public void setCompEmail(String compEmail) {
        this.compEmail = compEmail;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMobilePh() {
        return mobilePh;
    }

    public void setMobilePh(String mobilePh) {
        this.mobilePh = mobilePh;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    /**
     * @return the compImage
     */
    public String getCompImage() {
        return compImage;
    }

    /**
     * @param compImage the compImage to set
     */
    public void setCompImage(String compImage) {
        this.compImage = compImage;
    }
    
    
}
