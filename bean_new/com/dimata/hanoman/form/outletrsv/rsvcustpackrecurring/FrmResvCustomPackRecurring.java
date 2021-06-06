/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.hanoman.form.outletrsv.rsvcustpackrecurring;

import com.dimata.hanoman.entity.outletrsv.rsvcustpackrecurring.ResvCustomPackRecurring;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.util.Formater;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dewa
 */
public class FrmResvCustomPackRecurring extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ResvCustomPackRecurring entResvCustomPackRecurring;
    public static final String FRM_NAME_RESV_CUSTOM_PACK_RECURRING = "FRM_NAME_RESV_CUSTOM_PACK_RECURRING";
    public static final int FRM_FIELD_RSV_CUSTOME_PACK_RECUR_ID = 0;
    public static final int FRM_FIELD_REPEAT_TYPE = 1;
    public static final int FRM_FIELD_REPEAT_PERIODE = 2;
    public static final int FRM_FIELD_WEEK_DAYS = 3;
    public static final int FRM_FIELD_MONTHLY_TYPE = 4;
    public static final int FRM_FIELD_DAY_OF_MONTH = 5;
    public static final int FRM_FIELD_UNTIL_TYPE = 6;
    public static final int FRM_FIELD_REPEAT_NUMBER = 7;
    public static final int FRM_FIELD_REPEAT_UNTIL_DATE = 8;
    public static final int FRM_FIELD_CUSTOME_PACK_BILLING_ID = 9;
    public static final int FRM_FIELD_INVOICE_OPTION = 10;

    public static String[] fieldNames = {
        "FRM_FIELD_RSV_CUSTOME_PACK_RECUR_ID",
        "FRM_FIELD_REPEAT_TYPE",
        "FRM_FIELD_REPEAT_PERIODE",
        "FRM_FIELD_WEEK_DAYS",
        "FRM_FIELD_MONTHLY_TYPE",
        "FRM_FIELD_DAY_OF_MONTH",
        "FRM_FIELD_UNTIL_TYPE",
        "FRM_FIELD_REPEAT_NUMBER",
        "FRM_FIELD_REPEAT_UNTIL_DATE",
        "FRM_FIELD_CUSTOME_PACK_BILLING_ID",
        "FRM_FIELD_INVOICE_OPTION"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_INT
    };

    public FrmResvCustomPackRecurring() {
    }

    public FrmResvCustomPackRecurring(ResvCustomPackRecurring entResvCustomPackRecurring) {
        this.entResvCustomPackRecurring = entResvCustomPackRecurring;
    }

    public FrmResvCustomPackRecurring(HttpServletRequest request, ResvCustomPackRecurring entResvCustomPackRecurring) {
        super(new FrmResvCustomPackRecurring(entResvCustomPackRecurring), request);
        this.entResvCustomPackRecurring = entResvCustomPackRecurring;
    }

    public String getFormName() {
        return FRM_NAME_RESV_CUSTOM_PACK_RECURRING;
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

    public ResvCustomPackRecurring getEntityObject() {
        return entResvCustomPackRecurring;
    }

    public void requestEntityObject(ResvCustomPackRecurring entResvCustomPackRecurring, long oidPackBilling) {
        try {
            this.requestParam();
//            entResvCustomPackRecurring.setRsvCustomePackRecurId(getLong(FRM_FIELD_RSV_CUSTOME_PACK_RECUR_ID));
            entResvCustomPackRecurring.setRepeatType(getInt(FRM_FIELD_REPEAT_TYPE));
            entResvCustomPackRecurring.setRepeatPeriode(getInt(FRM_FIELD_REPEAT_PERIODE));

            Calendar calendar = Calendar.getInstance();
            Locale locales[] = Calendar.getAvailableLocales();

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);
            // for the current Locale :
            //   DateFormatSymbols symbols = new DateFormatSymbols(); 
            String[] dayNames = symbols.getShortWeekdays();
            for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
                boolean checked = true;//getParamBoolean(FrmResvCustomPackRecurring.fieldNames[FrmResvCustomPackRecurring.FRM_FIELD_WEEK_DAYS] + i);
                entResvCustomPackRecurring.setWeekDay(i, checked);
            }

            entResvCustomPackRecurring.setMonthlyType(getInt(FRM_FIELD_MONTHLY_TYPE));
            entResvCustomPackRecurring.setDayOfMonth(getInt(FRM_FIELD_DAY_OF_MONTH));
            entResvCustomPackRecurring.setUntilType(getInt(FRM_FIELD_UNTIL_TYPE));
            entResvCustomPackRecurring.setRepeatNumber(getInt(FRM_FIELD_REPEAT_NUMBER));
            entResvCustomPackRecurring.setRepeatUntilDate(Formater.formatDate(getString(FRM_FIELD_REPEAT_UNTIL_DATE), "yyyy-MM-dd"));
            entResvCustomPackRecurring.setCustomePackBillingId(oidPackBilling);
            entResvCustomPackRecurring.setInvoiceOption(getInt(FRM_FIELD_INVOICE_OPTION));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
