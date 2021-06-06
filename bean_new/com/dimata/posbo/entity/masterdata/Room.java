/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Dimata 007 - Mirahu 20120515
 */

/* package java */
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class Room extends Entity implements Serializable {

    public static final int NOT_AVAILABLE = 0;
    public static final int AVAILABLE = 1;
    public static final int ON_MAINTENANCE = 2;
    
    public static final String ROOM_STATUS_TITLE[] = {"Not Available", "Available", "On Maintenance"};
    
    public static final int ROOM_STATUS_TYPE[] = {0, 1, 2};
    
    private long locationId = 0;
    private String code = "";
    private String name = "";
    private String description = "";
    private int status = 0;
    private long roomClassId = 0;
    private int capacity = 0;
    private String capacityUnit = "";
    private int showIndex = 0;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRoomClassId() {
        return roomClassId;
    }

    public void setRoomClassId(long roomClassId) {
        this.roomClassId = roomClassId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCapacityUnit() {
        return capacityUnit;
    }

    public void setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
}
