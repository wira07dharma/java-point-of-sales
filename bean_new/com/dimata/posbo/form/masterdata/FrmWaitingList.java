/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.form.masterdata;

/* java package */
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.posbo.entity.masterdata.*;

public class FrmWaitingList extends FRMHandler implements I_FRMInterface, I_FRMType {

    private WaitingList waitingList;
    public static final String FRM_WAITING_LIST = "FRM_WAITING_LIST";
    public static final int FRM_CUSTOMER_WAITING_ID = 0;
    public static final int FRM_CUSTOMER_NAME = 1;
    public static final int FRM_NO_TLP = 2;
    public static final int FRM_START_TIME = 3;
    public static final int FRM_END_TIME = 4;
    public static final int FRM_REAL_END_TIME = 5;
    public static final int FRM_STATUS = 6;
    public static final int FRM_STAFF = 7;
    public static final int FRM_PAX = 8;
    public static String[] fieldNames =
            {
        "FRM_CUSTOMER_WAITING_ID", "FRM_CUSTOMER_NAME", "FRM_NO_TLP", "FRM_START_TIME","FRM_END_TIME","FRM_REAL_END_TIME","FRM_STATUS","FRM_STAFF","FRM_PAX"
    };
    public static int[] fieldTypes =
            {
        TYPE_LONG, 
        TYPE_STRING + ENTRY_REQUIRED, 
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT
    };

    public FrmWaitingList() {
    }

    public FrmWaitingList(WaitingList waitingList) {
        this.waitingList = waitingList;
    }

    public FrmWaitingList(HttpServletRequest request, WaitingList waitingList) {
        super(new FrmWaitingList(waitingList), request);


        this.waitingList = waitingList;
    }

    public String getFormName() {
        return FRM_WAITING_LIST;
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

    public WaitingList getEntityObject() {
        return waitingList;
    }

    public void requestEntityObject(WaitingList waitingList) {
        try {
            
            this.requestParam();
            waitingList.setCustomerName(getString(FRM_CUSTOMER_NAME));
            waitingList.setNoTlp(getString(FRM_NO_TLP));
            waitingList.setStartTime(getDate(FRM_START_TIME));
            waitingList.setEndTime(getDate(FRM_END_TIME));
            waitingList.setRealTime(getDate(FRM_REAL_END_TIME));
            waitingList.setStatus(getInt(FRM_STATUS));
            waitingList.setStaff(getString(FRM_STAFF));
            waitingList.setPax(getInt(FRM_PAX));
            
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
