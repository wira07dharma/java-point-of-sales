/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form;


import com.dimata.posbo.entity.masterdata.ClsSpending;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author IanRizky
 */
public class FrmClsSpending extends FRMHandler implements I_FRMInterface, I_FRMType {

	private ClsSpending entClsSpending;
	public static final String FRM_NAME_CLS_SPENDING = "FRM_NAME_CLS_SPENDING";
	public static final int FRM_FIELD_SPENDING_ID = 0;
	public static final int FRM_FIELD_TICKET_NAME = 1;
	public static final int FRM_FIELD_PRICE = 2;
	public static final int FRM_FIELD_MINIMUM_SPENDING = 3;
	public static final int FRM_FIELD_DETAILS = 4;
	public static final int FRM_FIELD_VALUE = 5;
	public static final int FRM_FIELD_EVENT_OID = 6;

	public static String[] fieldNames = {
		"FRM_FIELD_SPENDING_ID",
		"FRM_FIELD_TICKET_NAME",
		"FRM_FIELD_PRICE",
		"FRM_FIELD_MINIMUM_SPENDING",
		"FRM_FIELD_DETAILS",
		"FRM_FIELD_VALUE",
		"FRM_FIELD_EVENT_OID"
	};

	public static int[] fieldTypes = {
		TYPE_LONG,
		TYPE_STRING,
		TYPE_FLOAT,
		TYPE_FLOAT,
		TYPE_STRING,
		TYPE_FLOAT,
		TYPE_LONG
	};

	public FrmClsSpending() {
	}

	public FrmClsSpending(ClsSpending entClsSpending) {
		this.entClsSpending = entClsSpending;
	}

	public FrmClsSpending(HttpServletRequest request, ClsSpending entClsSpending) {
		super(new FrmClsSpending(entClsSpending), request);
		this.entClsSpending = entClsSpending;
	}

	public String getFormName() {
		return FRM_NAME_CLS_SPENDING;
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

	public ClsSpending getEntityObject() {
		return entClsSpending;
	}

	public void requestEntityObject(ClsSpending entClsSpending) {
		try {
			this.requestParam();
			entClsSpending.setTicketName(getString(FRM_FIELD_TICKET_NAME));
			entClsSpending.setPrice(getFloat(FRM_FIELD_PRICE));
			entClsSpending.setMinimumSpending(getFloat(FRM_FIELD_MINIMUM_SPENDING));
			entClsSpending.setDetails(getString(FRM_FIELD_DETAILS));
			entClsSpending.setValue(getFloat(FRM_FIELD_VALUE));
			entClsSpending.setEventOid(getLong(FRM_FIELD_EVENT_OID));
		} catch (Exception e) {
			System.out.println("Error on requestEntityObject : " + e.toString());
		}
	}

}
