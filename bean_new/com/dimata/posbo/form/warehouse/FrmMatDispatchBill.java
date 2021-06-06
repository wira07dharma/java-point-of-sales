/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.posbo.entity.warehouse.MatDispatchBill;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmMatDispatchBill extends FRMHandler implements I_FRMInterface, I_FRMType {

	private MatDispatchBill entMatDispatchBill;
	public static final String FRM_NAME_MATDISPATCHBILL = "FRM_NAME_MATDISPATCHBILL";
	public static final int FRM_FIELD_POS_DISPATCH_MATERIAL_ID = 0;
	public static final int FRM_FIELD_CASH_BILL_MAIN_ID = 1;
	public static final int FRM_FIELD_STATUS = 2;
	public static final int FRM_DISPATCH_MATERIAL_ITEM_ID = 3;

	public static String[] fieldNames = {
		"FRM_FIELD_POS_DISPATCH_MATERIAL_ID",
		"FRM_FIELD_CASH_BILL_MAIN_ID",
		"FRM_FIELD_STATUS",
		"DISPATCH_MATERIAL_ITEM_ID"
	};

	public static int[] fieldTypes = {
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT,
		TYPE_LONG
	};

	public FrmMatDispatchBill() {
	}

	public FrmMatDispatchBill(MatDispatchBill entMatDispatchBill) {
		this.entMatDispatchBill = entMatDispatchBill;
	}

	public FrmMatDispatchBill(HttpServletRequest request, MatDispatchBill entMatDispatchBill) {
		super(new FrmMatDispatchBill(entMatDispatchBill), request);
		this.entMatDispatchBill = entMatDispatchBill;
	}

	public String getFormName() {
		return FRM_NAME_MATDISPATCHBILL;
	}

	public int[] getFieldTypes() {
		return fieldTypes;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public int getFieldSize() {
		return fieldNames.length;
	}

	public MatDispatchBill getEntityObject() {
		return entMatDispatchBill;
	}

	public void requestEntityObject(MatDispatchBill entMatDispatchBill) {
		try {
			this.requestParam();
			entMatDispatchBill.setOID(getLong(FRM_FIELD_POS_DISPATCH_MATERIAL_ID));
			entMatDispatchBill.setCashBillOid(getLong(FRM_FIELD_CASH_BILL_MAIN_ID));
			entMatDispatchBill.setStatus(getInt(FRM_FIELD_STATUS));
			entMatDispatchBill.setMatItemOid(getLong(FRM_DISPATCH_MATERIAL_ITEM_ID));
		} catch (Exception e) {
			System.out.println("Error on requestEntityObject : " + e.toString());
		}
	}

}
