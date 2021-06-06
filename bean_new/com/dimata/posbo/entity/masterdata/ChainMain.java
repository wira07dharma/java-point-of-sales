/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author IanRizky
 */

import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class ChainMain  extends Entity {

	private long chainLocation = 0;
	private Date chainDate = null;
	private String chainNote = "";
	private int chainStatus = 0;
	private String chainTitle = "";

	public long getChainLocation() {
		return chainLocation;
	}

	public void setChainLocation(long chainLocation) {
		this.chainLocation = chainLocation;
	}

	public Date getChainDate() {
		return chainDate;
	}

	public void setChainDate(Date chainDate) {
		this.chainDate = chainDate;
	}

	public String getChainNote() {
		return chainNote;
	}

	public void setChainNote(String chainNote) {
		this.chainNote = chainNote;
	}

	public int getChainStatus() {
		return chainStatus;
	}

	public void setChainStatus(int chainStatus) {
		this.chainStatus = chainStatus;
	}

	/**
	 * @return the chainTitle
	 */
	public String getChainTitle() {
		return chainTitle;
	}

	/**
	 * @param chainTitle the chainTitle to set
	 */
	public void setChainTitle(String chainTitle) {
		this.chainTitle = chainTitle;
	}

}
