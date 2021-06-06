/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author Dimata 007
 * - Mirahu
 * 20120517
 */

/* package java */
import java.io.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class TableRoom extends Entity implements Serializable {
    private long locationId = 0;
    private long roomId = 0;
    private String tableNumber = "";
    private int dimentionL = 0;
    private int dimentionW = 0;
    private int capacity = 0;
    private int gridX = 0;
    private int gridY = 0;
    private String shape = "";
    private int status = 0;
    
    //ini untuk mengetahui table/meja masih kosong atau bersih
    private int statusTable = 0;

    
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
     * @return the roomId
     */
    public long getRoomId() {
        return roomId;
    }

    /**
     * @param roomId the roomId to set
     */
    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }


    /**
     * @return the dimentionL
     */
    public int getDimentionL() {
        return dimentionL;
    }

    /**
     * @param dimentionL the dimentionL to set
     */
    public void setDimentionL(int dimentionL) {
        this.dimentionL = dimentionL;
    }

    /**
     * @return the dimentionW
     */
    public int getDimentionW() {
        return dimentionW;
    }

    /**
     * @param dimentionW the dimentionW to set
     */
    public void setDimentionW(int dimentionW) {
        this.dimentionW = dimentionW;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the gridX
     */
    public int getGridX() {
        return gridX;
    }

    /**
     * @param gridX the gridX to set
     */
    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    /**
     * @return the gridY
     */
    public int getGridY() {
        return gridY;
    }

    /**
     * @param gridY the gridY to set
     */
    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    /**
     * @return the shape
     */
    public String getShape() {
        return shape;
    }

    /**
     * @param shape the shape to set
     */
    public void setShape(String shape) {
        this.shape = shape;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the tableNumber
     */
    public String getTableNumber() {
        return tableNumber;
    }

    /**
     * @param tableNumber the tableNumber to set
     */
    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    /**
     * @return the statusTable
     */
    public int getStatusTable() {
        return statusTable;
    }

    /**
     * @param statusTable the statusTable to set
     */
    public void setStatusTable(int statusTable) {
        this.statusTable = statusTable;
    }
    
    
    
}
