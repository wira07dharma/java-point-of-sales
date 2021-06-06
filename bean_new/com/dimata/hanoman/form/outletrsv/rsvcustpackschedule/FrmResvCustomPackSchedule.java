/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.rsvcustpackschedule;

import com.dimata.hanoman.entity.outletrsv.rsvcustpackschedule.ResvCustomPackSchedule;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmResvCustomPackSchedule extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ResvCustomPackSchedule entResvCustomPackSchedule;
    public static final String FRM_NAME_RESV_CUSTOM_PACK_SCHEDULE = "FRM_NAME_RESV_CUSTOM_PACK_SCHEDULE";
    public static final int FRM_FIELD_CUSTOM_SCHEDULE_ID = 0;
    public static final int FRM_FIELD_CUSTOME_PACK_BILLING_ID = 1;
    public static final int FRM_FIELD_ROOM_ID = 2;
    public static final int FRM_FIELD_TABLE_ID = 3;
    public static final int FRM_FIELD_START_DATE = 4;
    public static final int FRM_FIELD_END_DATE = 5;
    public static final int FRM_FIELD_STATUS = 6;
    public static final int FRM_FIELD_QUANTITY = 7;
    public static final int FRM_FIELD_START_DATE2 = 8;

    public static String[] fieldNames = {
        "FRM_FIELD_CUSTOM_SCHEDULE_ID",
        "FRM_FIELD_CUSTOME_PACK_BILLING_ID",
        "FRM_FIELD_ROOM_ID",
        "FRM_FIELD_TABLE_ID",
        "FRM_FIELD_START_DATE",
        "FRM_FIELD_END_DATE",
        "FRM_FIELD_STATUS",
        "FRM_FIELD_QUANTITY",
        "FRM_FIELD_START_DATE2"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_DATE
    };

    public FrmResvCustomPackSchedule() {
    }

    public FrmResvCustomPackSchedule(ResvCustomPackSchedule entResvCustomPackSchedule) {
        this.entResvCustomPackSchedule = entResvCustomPackSchedule;
    }

    public FrmResvCustomPackSchedule(HttpServletRequest request, ResvCustomPackSchedule entResvCustomPackSchedule) {
        super(new FrmResvCustomPackSchedule(entResvCustomPackSchedule), request);
        this.entResvCustomPackSchedule = entResvCustomPackSchedule;
    }

    public String getFormName() {
        return FRM_NAME_RESV_CUSTOM_PACK_SCHEDULE;
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

    public ResvCustomPackSchedule getEntityObject() {
        return entResvCustomPackSchedule;
    }

    public void requestEntityObject(ResvCustomPackSchedule entResvCustomPackSchedule, long oidPackBilling) {
        try {
            this.requestParam();
            String date = getString(FRM_FIELD_START_DATE);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate = dateFormat.parse(date);
//            entResvCustomPackSchedule.setCustomScheduleId(getLong(FRM_FIELD_CUSTOM_SCHEDULE_ID));
            entResvCustomPackSchedule.setCustomePackBillingId(oidPackBilling);
            entResvCustomPackSchedule.setRoomId(getLong(FRM_FIELD_ROOM_ID));
            entResvCustomPackSchedule.setTableId(getLong(FRM_FIELD_TABLE_ID));
            entResvCustomPackSchedule.setStartDate(startDate);
            entResvCustomPackSchedule.setEndDate(getDate(FRM_FIELD_END_DATE));
            entResvCustomPackSchedule.setStatus(getInt(FRM_FIELD_STATUS));
            entResvCustomPackSchedule.setQuantity(getFloat(FRM_FIELD_QUANTITY));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

    public void requestEntityObjectRecurring(ResvCustomPackSchedule entResvCustomPackSchedule, long oidPackBilling, Date startDate, Date endDate) {
        try {
            this.requestParam();
//            entResvCustomPackSchedule.setCustomScheduleId(getLong(FRM_FIELD_CUSTOM_SCHEDULE_ID));
            entResvCustomPackSchedule.setCustomePackBillingId(oidPackBilling);
            entResvCustomPackSchedule.setRoomId(getLong(FRM_FIELD_ROOM_ID));
            entResvCustomPackSchedule.setTableId(getLong(FRM_FIELD_TABLE_ID));
            entResvCustomPackSchedule.setStartDate(startDate);
            entResvCustomPackSchedule.setEndDate(endDate);
            entResvCustomPackSchedule.setStatus(getInt(FRM_FIELD_STATUS));
            entResvCustomPackSchedule.setQuantity(getFloat(FRM_FIELD_QUANTITY));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
    
    public void requestEntityObjectRecurringByDate(ResvCustomPackSchedule entResvCustomPackSchedule, long oidPackBilling) {
        try {
            this.requestParam();
            Date startDate = null;
            String date = getString(FRM_FIELD_START_DATE2);
            if (date != "") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                startDate = dateFormat.parse(date);
            }

//            entResvCustomPackSchedule.setCustomScheduleId(getLong(FRM_FIELD_CUSTOM_SCHEDULE_ID));
            entResvCustomPackSchedule.setCustomePackBillingId(oidPackBilling);
            entResvCustomPackSchedule.setRoomId(getLong(FRM_FIELD_ROOM_ID));
            entResvCustomPackSchedule.setTableId(getLong(FRM_FIELD_TABLE_ID));
            entResvCustomPackSchedule.setStartDate(startDate);
            entResvCustomPackSchedule.setEndDate(getDate(FRM_FIELD_END_DATE));
            entResvCustomPackSchedule.setStatus(getInt(FRM_FIELD_STATUS));
            entResvCustomPackSchedule.setQuantity(getFloat(FRM_FIELD_QUANTITY));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
