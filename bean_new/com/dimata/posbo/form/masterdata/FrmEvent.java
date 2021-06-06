/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

import com.dimata.posbo.entity.masterdata.Event;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmEvent extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Event entEvent;
    public static final String FRM_NAME_EVENT = "FRM_NAME_EVENT";
    public static final int FRM_FIELD_EVENT_OID = 0;
    public static final int FRM_FIELD_EVENT_CODE = 1;
    public static final int FRM_FIELD_EVENT_TITLE = 2;
    public static final int FRM_FIELD_DESCRIPTION = 3;
    public static final int FRM_FIELD_EVENT_DATETIME = 4;
    public static final int FRM_FIELD_PRICE_TYPE_ID = 5;
    public static final int FRM_FIELD_TAG_DEPOSIT = 6;
    public static final int FRM_FIELD_COMPANY_ID = 7;
    public static final int FRM_FIELD_EVENT_END_DATETIME = 8;
    public static final int FRM_FIELD_CURRENCY_TYPE_ID = 9;
	public static final int FRM_FIELD_REFUND_ENABLE = 10;
	public static final int FRM_FIELD_LIMIT_REFUND_DATE = 11;
	public static final int FRM_FIELD_REFUND_MODE = 12;
	public static final int FRM_FIELD_LOCATION_ID = 13;

    public static String[] fieldNames = {
        "FRM_FIELD_EVENT_OID",
        "FRM_FIELD_EVENT_CODE",
        "FRM_FIELD_EVENT_TITLE",
        "FRM_FIELD_DESCRIPTION",
        "FRM_FIELD_EVENT_DATETIME",
        "FRM_FIELD_PRICE_TYPE_ID",
        "FRM_FIELD_TAG_DEPOSIT",
        "FRM_FIELD_COMPANY_ID",
        "FRM_FIELD_EVENT_END_DATETIME",
        "CURRENCY_TYPE_ID",
		"FRM_FIELD_REFUND_ENABLE",
		"FRM_FIELD_LIMIT_REFUND_DATE",
		"FRM_FIELD_REFUND_MODE",
		"FRM_FIELD_LOCATION_ID"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
		TYPE_INT,
		TYPE_STRING,
		TYPE_INT,
		TYPE_LONG
    };

    public FrmEvent() {
    }

    public FrmEvent(Event entEvent) {
        this.entEvent = entEvent;
    }

    public FrmEvent(HttpServletRequest request, Event entEvent) {
        super(new FrmEvent(entEvent), request);
        this.entEvent = entEvent;
    }

    public String getFormName() {
        return FRM_NAME_EVENT;
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

    public Event getEntityObject() {
        return entEvent;
    }

    public void requestEntityObject(Event entEvent) {
        try {
            this.requestParam();
            entEvent.setEventCode(getString(FRM_FIELD_EVENT_CODE));
            entEvent.setEventTitle(getString(FRM_FIELD_EVENT_TITLE));
            entEvent.setDescription(getString(FRM_FIELD_DESCRIPTION));
            entEvent.setEventDatetime(Formater.formatDate(getString(FRM_FIELD_EVENT_DATETIME), "yyyy-MM-dd HH:mm"));
            entEvent.setPriceTypeId(getLong(FRM_FIELD_PRICE_TYPE_ID));
            entEvent.setTagDeposit(getDouble(FRM_FIELD_TAG_DEPOSIT));
            entEvent.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));
            entEvent.setEventEndDatetime(Formater.formatDate(getString(FRM_FIELD_EVENT_END_DATETIME), "yyyy-MM-dd HH:mm"));
            entEvent.setCurrencyTypeId(getLong(FRM_FIELD_CURRENCY_TYPE_ID));
			entEvent.setEnableRefund(getInt(FRM_FIELD_REFUND_ENABLE));
			entEvent.setLimitRefund(Formater.formatDate(getString(FRM_FIELD_LIMIT_REFUND_DATE), "yyyy-MM-dd HH:mm"));
			entEvent.setRefundMode(getInt(FRM_FIELD_REFUND_MODE));
			entEvent.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
