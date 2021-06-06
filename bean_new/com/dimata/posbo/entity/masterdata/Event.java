/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class Event extends Entity {

    private String eventCode = "";
    private String eventTitle = "";
    private String description = "";
    private Date eventDatetime = null;
    private long priceTypeId = 0;
    private double tagDeposit = 0;
    private long companyId = 0;
    private Date eventEndDatetime = null;
    private long currencyTypeId = 0;
	private int enableRefund = 0;
	private Date limitRefund = null;
	private int refundMode = 0;
	private long locationId = 0;

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventDatetime() {
        return eventDatetime;
    }

    public void setEventDatetime(Date eventDatetime) {
        this.eventDatetime = eventDatetime;
    }

    public long getPriceTypeId() {
        return priceTypeId;
    }

    public void setPriceTypeId(long priceTypeId) {
        this.priceTypeId = priceTypeId;
    }

    public double getTagDeposit() {
        return tagDeposit;
    }

    public void setTagDeposit(double tagDeposit) {
        this.tagDeposit = tagDeposit;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public Date getEventEndDatetime() {
        return eventEndDatetime;
    }

    public void setEventEndDatetime(Date eventEndDatetime) {
        this.eventEndDatetime = eventEndDatetime;
    }

    public long getCurrencyTypeId() {
        return currencyTypeId;
    }

    public void setCurrencyTypeId(long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

	/**
	 * @return the enableRefund
	 */
	public int getEnableRefund() {
		return enableRefund;
	}

	/**
	 * @param enableRefund the enableRefund to set
	 */
	public void setEnableRefund(int enableRefund) {
		this.enableRefund = enableRefund;
	}

	/**
	 * @return the limitRefund
	 */
	public Date getLimitRefund() {
		return limitRefund;
	}

	/**
	 * @param limitRefund the limitRefund to set
	 */
	public void setLimitRefund(Date limitRefund) {
		this.limitRefund = limitRefund;
	}

	/**
	 * @return the refundMode
	 */
	public int getRefundMode() {
		return refundMode;
	}

	/**
	 * @param refundMode the refundMode to set
	 */
	public void setRefundMode(int refundMode) {
		this.refundMode = refundMode;
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

}
