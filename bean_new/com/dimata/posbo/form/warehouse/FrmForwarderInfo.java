/*
 * FrmForwarderInfo.java
 *
 * Created on June 4, 2007, 10:44 AM
 */

package com.dimata.posbo.form.warehouse;

/** java package  */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** qdep package */
import com.dimata.qdep.form.*;

/** project package */
import com.dimata.posbo.entity.warehouse.ForwarderInfo;

/**
 *
 * @author  gwawan
 */
public class FrmForwarderInfo extends FRMHandler implements I_FRMInterface, I_FRMType {
    private ForwarderInfo forwarderInfo;
    
    public static final String FRM_NAME_FORWARDER_INFO = "FRM_NAME_FORWARDER_INFO";
    public static final int FRM_FIELD_FORWARDER_INFO_ID = 0;
    public static final int FRM_FIELD_RECEIVE_ID = 1;
    public static final int FRM_FIELD_FI_LOCATION_ID = 2;
    public static final int FRM_FIELD_DOC_NUMBER = 3;
    public static final int FRM_FIELD_DOC_DATE = 4;
    public static final int FRM_FIELD_CONTACT_ID = 5;
    public static final int FRM_FIELD_FI_CURRENCY_ID = 6;
    public static final int FRM_FIELD_FI_TRANS_RATE = 7;
    public static final int FRM_FIELD_TOTAL_COST = 8;
    public static final int FRM_FIELD_NOTES = 9;
    public static final int FRM_FIELD_FI_STATUS = 10;
    public static final int FRM_FIELD_COUNTER = 11;
    
    /** Creates a new instance of FrmForwarderInfo */
    public FrmForwarderInfo() {
    }
    
    public FrmForwarderInfo(ForwarderInfo forwarderInfo) {
        this.forwarderInfo = forwarderInfo;
    }
    
    public FrmForwarderInfo(HttpServletRequest request, ForwarderInfo forwarderInfo) {
        super(new FrmForwarderInfo(forwarderInfo), request);
        this.forwarderInfo = forwarderInfo;
    }
    
    public static String[] fieldNames = {
        "FRM_FIELD_FORWARDER_INFO_ID",
        "FRM_FIELD_RECEIVE_ID",
        "FRM_FIELD_FI_LOCATION_ID",
        "FRM_FIELD_DOC_NUMBER",
        "FRM_FIELD_DOC_DATE",
        "FRM_FIELD_CONTACT_ID",
        "FRM_FIELD_FI_CURRENCY_ID",
        "FRM_FIELD_FI_TRANS_RATE",
        "FRM_FIELD_TOTAL_COST",
        "FRM_FIELD_NOTES",
        "FRM_FIELD_FI_STATUS",
        "FRM_FIELD_COUNTER"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT
    };
    
    public String[] getFieldNames() {
        return this.fieldNames;
    };
    
    public int getFieldSize() {
        return this.fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return this.fieldTypes;
    }
    
    public String getFormName() {
        return this.FRM_NAME_FORWARDER_INFO;
    }
    
    public ForwarderInfo getEntitybject() {
        return this.forwarderInfo;
    }
    
    public void requestEntityObject(ForwarderInfo forwarderInfo) {
        try {
            this.requestParam();
            forwarderInfo.setOID(getLong(FRM_FIELD_FORWARDER_INFO_ID));
            forwarderInfo.setReceiveId(getLong(FRM_FIELD_RECEIVE_ID));
            forwarderInfo.setLocationId(getLong(FRM_FIELD_FI_LOCATION_ID));
            forwarderInfo.setDocNumber(getString(FRM_FIELD_DOC_NUMBER));
            forwarderInfo.setDocDate(getDate(FRM_FIELD_DOC_DATE));
            forwarderInfo.setContactId(getLong(FRM_FIELD_CONTACT_ID));
            forwarderInfo.setCurrencyId(getLong(FRM_FIELD_FI_CURRENCY_ID));
            forwarderInfo.setTransRate(getDouble(FRM_FIELD_FI_TRANS_RATE));
            forwarderInfo.setTotalCost(getDouble(FRM_FIELD_TOTAL_COST));
            forwarderInfo.setNotes(getString(FRM_FIELD_NOTES));
            forwarderInfo.setStatus(getInt(FRM_FIELD_FI_STATUS));
            forwarderInfo.setCounter(getInt(FRM_FIELD_COUNTER));
        }
        catch(Exception e) {
            System.out.println("Exc in requestEntityObject >>> "+e.toString());
        }
    }
    
}
