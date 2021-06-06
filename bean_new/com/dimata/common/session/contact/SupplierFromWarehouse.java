/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.session.contact;

/**
 *
 * @author dimata005
 */
public class SupplierFromWarehouse {
    private long supplierId=0;
    private String locationName="";
    private long contactId=0;

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

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the contactId
     */
    public long getContactId() {
        return contactId;
    }

    /**
     * @param contactId the contactId to set
     */
    public void setContactId(long contactId) {
        this.contactId = contactId;
    }
    
}

