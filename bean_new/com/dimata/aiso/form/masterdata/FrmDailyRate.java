/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.DailyRate;
import com.dimata.aiso.entity.masterdata.PstDailyRate;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmDailyRate extends FRMHandler implements I_FRMInterface, I_FRMType{

    public static final String FRM_DAILY_RATE = "FRM_DAILY_RATE";   
       
    public static final int FRM_CURRENCY_ID = 0;
    public static final int FRM_BUYING_AMOUNT = 1;
    public static final int FRM_SELLING_AMOUNT = 2;
    public static final int FRM_STATUS = 3;
    public static final int FRM_START_DATE = 4;
    public static final int FRM_END_DATE = 5;
    public static final int FRM_LOCAL_FOREIGN = 6;

    public static String[] fieldNames =
            {
                "FRM_CURRENCY_ID",
                "FRM_BUYING_AMOUNT",
                "FRM_SELLING_AMOUNT",
                "FRM_STATUS",
                "FRM_START_DATE",
                "FRM_END_DATE",
                "FRM_LOCAL_FOREIGN"
            };

    public static int[] fieldTypes =
            {
                TYPE_LONG + ENTRY_REQUIRED,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_INT,
                TYPE_DATE,
                TYPE_DATE,
                TYPE_INT
            };

    private DailyRate objDailyRate;

    public FrmDailyRate(DailyRate objDailyRate) {
        this.objDailyRate = objDailyRate;
    }

    public FrmDailyRate(HttpServletRequest request, DailyRate objDailyRate) {
        super(new FrmDailyRate(objDailyRate), request);
        this.objDailyRate = objDailyRate;
    }

    public String getFormName() {
        return FRM_DAILY_RATE;
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

    public DailyRate getEntityObject() {
        return objDailyRate;
    }

    public void requestEntityObject(DailyRate objDailyRate) {
        try {
            this.requestParam();
            objDailyRate.setCurrencyId(this.getLong(FRM_CURRENCY_ID));
            objDailyRate.setBuyingAmount(this.getDouble(FRM_BUYING_AMOUNT));
            objDailyRate.setSellingAmount(this.getDouble(FRM_SELLING_AMOUNT));
            objDailyRate.setStatus(PstDailyRate.ACTIVE);
            objDailyRate.setStartDate(this.getDate(FRM_START_DATE));
            objDailyRate.setEndDate(this.getDate(FRM_END_DATE));            
            objDailyRate.setLocalForeign(this.getInt(FRM_LOCAL_FOREIGN));
            
            this.objDailyRate = objDailyRate;
        } catch (Exception e) {
            objDailyRate = new DailyRate();
        }
    }
}
