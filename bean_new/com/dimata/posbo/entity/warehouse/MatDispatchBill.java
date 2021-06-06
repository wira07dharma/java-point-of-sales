/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;

public class MatDispatchBill extends Entity {

	private long cashBillOid = 0;
	private int status = 0;
	private long matItemOid = 0;

	public long getCashBillOid() {
		return cashBillOid;
	}

	public void setCashBillOid(long cashBillOid) {
		this.cashBillOid = cashBillOid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the matItemOid
	 */
	public long getMatItemOid() {
		return matItemOid;
	}

	/**
	 * @param matItemOid the matItemOid to set
	 */
	public void setMatItemOid(long matItemOid) {
		this.matItemOid = matItemOid;
	}

	
	
}
