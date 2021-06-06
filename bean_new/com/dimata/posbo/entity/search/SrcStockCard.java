/**
 * Created by IntelliJ IDEA. User: gadnyana Date: Feb 25, 2005 Time: 10:00:19 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.search;

import java.util.Date;
import java.util.Vector;

public class SrcStockCard {

    private int prevDays = 0;
    private long locationId = 0;
    private long materialId = 0;
    private Date stardDate = new Date();
    private Date endDate = new Date();
    private Vector docStatus = new Vector(1, 1);
    private int language = 0;
    private int statusDate;
    //qtyAll
    private double qty = 0;
    //added by dewok 20190305, for warehouse location if has one
    private long warehouseLocationId = 0;

    public int getPrevDays() {
        return prevDays;
    }

    public void setPrevDays(int prevDays) {
        this.prevDays = prevDays;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public Date getStardDate() {
        return stardDate;
    }

    public void setStardDate(Date stardDate) {
        this.stardDate = stardDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Vector getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(Vector docStatus) {
        this.docStatus = docStatus;
    }

    /**
     * @return the qty
     */
    public double getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(double qty) {
        this.qty = qty;
    }

    /**
     * @return the language
     */
    public int getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(int language) {
        this.language = language;
    }

    /**
     * @return the statusDate
     */
    public int getStatusDate() {
        return statusDate;
    }

    /**
     * @param statusDate the statusDate to set
     */
    public void setStatusDate(int statusDate) {
        this.statusDate = statusDate;
    }

    public long getWarehouseLocationId() {
        return warehouseLocationId;
    }

    public void setWarehouseLocationId(long warehouseLocationId) {
        this.warehouseLocationId = warehouseLocationId;
    }

}
