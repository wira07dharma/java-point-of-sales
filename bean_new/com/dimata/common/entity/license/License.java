/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.license;

/**
 *
 * @author dimata005
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class License extends Entity {

    private long customerId = 0;
    private long productId = 0;
    private int versionSystem = 0;
    private Date dateOperasi = null;
    private int validDay = 0;
    private String databaseUsername = "";
    private String databasePassword = "";
    private int periode=0;
    
    private String customerName="";
    private String productName="";
    private String databaseUsernameEnkrip="";
    private String databasePasswordEnkrip="";
    
    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }


    public Date getDateOperasi() {
        return dateOperasi;
    }

    public void setDateOperasi(Date dateOperasi) {
        this.dateOperasi = dateOperasi;
    }

    public int getValidDay() {
        return validDay;
    }

    public void setValidDay(int validDay) {
        this.validDay = validDay;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    /**
     * @return the versionSystem
     */
    public int getVersionSystem() {
        return versionSystem;
    }

    /**
     * @param versionSystem the versionSystem to set
     */
    public void setVersionSystem(int versionSystem) {
        this.versionSystem = versionSystem;
    }

    /**
     * @return the periode
     */
    public int getPeriode() {
        return periode;
    }

    /**
     * @param periode the periode to set
     */
    public void setPeriode(int periode) {
        this.periode = periode;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the databaseUsernameEnkrip
     */
    public String getDatabaseUsernameEnkrip() {
        return databaseUsernameEnkrip;
    }

    /**
     * @param databaseUsernameEnkrip the databaseUsernameEnkrip to set
     */
    public void setDatabaseUsernameEnkrip(String databaseUsernameEnkrip) {
        this.databaseUsernameEnkrip = databaseUsernameEnkrip;
    }

    /**
     * @return the databasePasswordEnkrip
     */
    public String getDatabasePasswordEnkrip() {
        return databasePasswordEnkrip;
    }

    /**
     * @param databasePasswordEnkrip the databasePasswordEnkrip to set
     */
    public void setDatabasePasswordEnkrip(String databasePasswordEnkrip) {
        this.databasePasswordEnkrip = databasePasswordEnkrip;
    }

}
