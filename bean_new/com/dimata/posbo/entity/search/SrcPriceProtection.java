/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.search;

import java.util.Date;

/**
 *
 * @author dimata005
 */
public class SrcPriceProtection {
    private Date dateFrom = new Date();
    private Date dateTo = new Date();
    private long locationId;
    private String ppNumber="";
    private long supplierId;
    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the ppNumber
     */
    public String getPpNumber() {
        return ppNumber;
    }

    /**
     * @param ppNumber the ppNumber to set
     */
    public void setPpNumber(String ppNumber) {
        this.ppNumber = ppNumber;
    }

    /**
     * @return the supplierId
     */
    public long getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }
    
    
}
