package com.dimata.posbo.entity.search;

/* package java */
import java.util.*;

public class SrcMatConReceive {
    
    private Vector receivestatus = new Vector();
    private String receivenumber = "";
    private String vendorname = "";
    private Date receivefromdate = new Date();
    private Date receivetodate = new Date();
    private int receivedatestatus = 0;
    private int receivesortby = 0;
    private int receiveSortByType = 0;
    private int locationType = -1;
    private long locationFrom = 0;
    private long locationId = 0;
    private int receiveSource = -1;
    private String invoiceSupplier = "";
    
    public static String strSortType[][] = {
        {"Menaik","Menurun"},
        {"Ascending","Descending"}
    };
    
    public static Vector getStrSortType(int language) {
        Vector result = new Vector(1,1);
        for(int i=0; i<strSortType.length; i++) {
            result.add(String.valueOf(strSortType[language][i]));
        }
        return result;
    }
    
    public Vector getReceivestatus() {
        return receivestatus;
    }
    
    public void setReceivestatus(Vector receivestatus) {
        this.receivestatus = receivestatus;
    }
    
    public String getReceivenumber() {
        return receivenumber;
    }
    
    public void setReceivenumber(String receivenumber) {
        if ( receivenumber == null ) {
            receivenumber = "";
        }
        this.receivenumber = receivenumber;
    }
    
    public String getVendorname() {
        return vendorname;
    }
    
    public void setVendorname(String vendorname) {
        if ( vendorname == null ) {
            vendorname = "";
        }
        this.vendorname = vendorname;
    }
    
    public Date getReceivefromdate() {
        return receivefromdate;
    }
    
    public void setReceivefromdate(Date receivefromdate) {
        this.receivefromdate = receivefromdate;
    }
    
    public Date getReceivetodate() {
        return receivetodate;
    }
    
    public void setReceivetodate(Date receivetodate) {
        this.receivetodate = receivetodate;
    }
    
    public int getReceivedatestatus() {
        return receivedatestatus;
    }
    
    public void setReceivedatestatus(int receivedatestatus) {
        this.receivedatestatus = receivedatestatus;
    }
    
    public int getReceivesortby() {
        return receivesortby;
    }
    
    public void setReceivesortby(int receivesortby) {
        this.receivesortby = receivesortby;
    }
    
    public int getReceiveSortByType() {
        return receiveSortByType;
    }
    
    public void setReceiveSortByType(int receiveSortByType) {
        this.receiveSortByType = receiveSortByType;
    }
    
    public int getLocationType() {
        return locationType;
    }
    
    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }
    
    public long getLocationFrom() {
        return locationFrom;
    }
    
    public void setLocationFrom(long locationFrom) {
        this.locationFrom = locationFrom;
    }
    
    public long getLocationId() {
        return locationId;
    }
    
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    
    public int getReceiveSource() {
        return receiveSource;
    }
    
    public void setReceiveSource(int receiveSource) {
        this.receiveSource = receiveSource;
    }
    
    public String getInvoiceSupplier() {
        return invoiceSupplier;
    }
    
    public void setInvoiceSupplier(String invoiceSupplier) {
        if ( invoiceSupplier == null ) {
            invoiceSupplier = "";
        }
        this.invoiceSupplier = invoiceSupplier;
    }
    
}
