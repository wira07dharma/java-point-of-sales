/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author IanRizky
 */
public class ClsSpending extends Entity {

	private String ticketName = "";
	private double price = 0;
	private double minimumSpending = 0;
	private String details = "";
	private double value = 0;
	private long eventOid = 0;

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMinimumSpending() {
		return minimumSpending;
	}

	public void setMinimumSpending(double minimumSpending) {
		this.minimumSpending = minimumSpending;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public long getEventOid() {
		return eventOid;
	}

	public void setEventOid(long eventOid) {
		this.eventOid = eventOid;
	}

}
