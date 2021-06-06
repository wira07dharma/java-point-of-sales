/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author dimata005
 */
public class WaitingList extends Entity{
    private String customerName="";
    private String noTlp="";
    private Date startTime = new Date();
    private Date endTime=new Date();
    private Date realTime=new Date();
    private int status=0;
    private String staff="";
    private int pax=0;
    
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
     * @return the noTlp
     */
    public String getNoTlp() {
        return noTlp;
    }

    /**
     * @param noTlp the noTlp to set
     */
    public void setNoTlp(String noTlp) {
        this.noTlp = noTlp;
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the realTime
     */
    public Date getRealTime() {
        return realTime;
    }

    /**
     * @param realTime the realTime to set
     */
    public void setRealTime(Date realTime) {
        this.realTime = realTime;
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
     * @return the staff
     */
    public String getStaff() {
        return staff;
    }

    /**
     * @param staff the staff to set
     */
    public void setStaff(String staff) {
        this.staff = staff;
    }

    /**
     * @return the pax
     */
    public int getPax() {
        return pax;
    }

    /**
     * @param pax the pax to set
     */
    public void setPax(int pax) {
        this.pax = pax;
    }
    
    
}
