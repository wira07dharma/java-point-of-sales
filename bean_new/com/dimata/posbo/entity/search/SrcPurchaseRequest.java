/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.search;

import java.util.Date;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class SrcPurchaseRequest {
    private Vector prmstatus = new Vector();
    private String prmnumber = "";
    private Date prmdatefrom = new Date();
    private Date prmdateto = new Date();
    private long location;
    private int statusdate;
    private int sortby;
    private int sortByType;
    private int locationType = -1;
    
    private int supplierWarehouse=0;
    /**
     * @return the prmstatus
     */
    
    public static final String textListSortByKey[] = {"Kode", "Tanggal", "Status", "Supplier"};
    public static final String textListSortByValue[] = {"0", "1", "2", "3"};
    
    public static final String textListSortRequestKey[] = {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status"};
    public static final String textListSortRequestValue[] = {"0", "1", "2", "3", "4"};
    
    public static final String statusKey[] = {"Draft", "To Be Confirm", "Approved", "Final", "Closed", "Posted"};
    public static final String statusValue[] = {"0", "1", "10", "2", "5", "7"};
    
    public static final String statusRequestKey[] = {"Draft", "Final", "Closed"};
    public static final String statusRequestValue[] = {"0", "2", "5"};
    
    public Vector getPrmstatus() {
        return prmstatus;
    }

    /**
     * @param prmstatus the prmstatus to set
     */
    public void setPrmstatus(Vector prmstatus) {
        this.prmstatus = prmstatus;
    }

    /**
     * @return the prmnumber
     */
    public String getPrmnumber() {
        return prmnumber;
    }

    /**
     * @param prmnumber the prmnumber to set
     */
    public void setPrmnumber(String prmnumber) {
        this.prmnumber = prmnumber;
    }

    /**
     * @return the prmdatefrom
     */
    public Date getPrmdatefrom() {
        return prmdatefrom;
    }

    /**
     * @param prmdatefrom the prmdatefrom to set
     */
    public void setPrmdatefrom(Date prmdatefrom) {
        this.prmdatefrom = prmdatefrom;
    }

    /**
     * @return the prmdateto
     */
    public Date getPrmdateto() {
        return prmdateto;
    }

    /**
     * @param prmdateto the prmdateto to set
     */
    public void setPrmdateto(Date prmdateto) {
        this.prmdateto = prmdateto;
    }

    /**
     * @return the location
     */
    public long getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(long location) {
        this.location = location;
    }

    /**
     * @return the statusdate
     */
    public int getStatusdate() {
        return statusdate;
    }

    /**
     * @param statusdate the statusdate to set
     */
    public void setStatusdate(int statusdate) {
        this.statusdate = statusdate;
    }

    /**
     * @return the sortby
     */
    public int getSortby() {
        return sortby;
    }

    /**
     * @param sortby the sortby to set
     */
    public void setSortby(int sortby) {
        this.sortby = sortby;
    }

    /**
     * @return the sortByType
     */
    public int getSortByType() {
        return sortByType;
    }

    /**
     * @param sortByType the sortByType to set
     */
    public void setSortByType(int sortByType) {
        this.sortByType = sortByType;
    }

    /**
     * @return the locationType
     */
    public int getLocationType() {
        return locationType;
    }

    /**
     * @param locationType the locationType to set
     */
    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    /**
     * @return the supplierWarehouse
     */
    public int getSupplierWarehouse() {
        return supplierWarehouse;
    }

    /**
     * @param supplierWarehouse the supplierWarehouse to set
     */
    public void setSupplierWarehouse(int supplierWarehouse) {
        this.supplierWarehouse = supplierWarehouse;
    }

    
}
