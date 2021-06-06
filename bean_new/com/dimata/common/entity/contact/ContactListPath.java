package com.dimata.common.entity.contact;

/**
 *
 * @author Witar
 */
import com.dimata.qdep.entity.Entity;

public class ContactListPath extends Entity {

    private long contactId = 0;
    private String address = "";
    private double latitude = 0;
    private double longitude = 0;

    public long getContactId(){
        return contactId;
    }

    public void setContactId(long contactId){
        this.contactId = contactId;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

}
