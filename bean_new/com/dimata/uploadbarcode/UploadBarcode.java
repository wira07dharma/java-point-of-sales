package com.dimata.uploadbarcode;

import java.util.Vector;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Jul 19, 2007
 * Time: 11:59:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class UploadBarcode {

    private Vector listBarcode = new Vector(1, 1);
    private long locationOid = 0;
    private long supplierOid = 0;
    private long toLocationOid = 0;
    private Date date = new Date();
    private String desc = "";


    public Vector getListBarcode() {
        return listBarcode;
    }

    public void setListBarcode(Vector listBarcode) {
        this.listBarcode = listBarcode;
    }

    public long getLocationOid() {
        return locationOid;
    }

    public void setLocationOid(long locationOid) {
        this.locationOid = locationOid;
    }

    public long getSupplierOid() {
        return supplierOid;
    }

    public void setSupplierOid(long supplierOid) {
        this.supplierOid = supplierOid;
    }

    public long getToLocationOid() {
        return toLocationOid;
    }

    public void setToLocationOid(long toLocationOid) {
        this.toLocationOid = toLocationOid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
