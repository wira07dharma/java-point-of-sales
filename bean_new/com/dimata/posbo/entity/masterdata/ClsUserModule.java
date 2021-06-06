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

public class ClsUserModule extends Entity {

	private long userId = 0;
	private long eventOID = 0;
	private int idModulEnable = 0;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getEventOID() {
		return eventOID;
	}

	public void setEventOID(long eventOID) {
		this.eventOID = eventOID;
	}

	public int getIdModulEnable() {
		return idModulEnable;
	}

	public void setIdModulEnable(int idModulEnable) {
		this.idModulEnable = idModulEnable;
	}

}
