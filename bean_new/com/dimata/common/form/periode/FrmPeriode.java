/*
 * Form Name  	:  FrmMcdPeriode.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	:  [authorName]
 * @version  	:  [version]
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.common.form.periode;

import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.common.entity.periode.*;

public class FrmPeriode extends FRMHandler implements I_FRMInterface, I_FRMType {
    private Periode periode;
    public static final String FRM_NAME_PERIODE = "FRM_NAME_PERIODE";
    public static final int FRM_FIELD_STOCK_PERIODE_ID = 0;
    public static final int FRM_FIELD_PERIODE_TYPE = 1;
    public static final int FRM_FIELD_PERIODE_NAME = 2;
    public static final int FRM_FIELD_START_DATE = 3;
    public static final int FRM_FIELD_END_DATE = 4;
    public static final int FRM_FIELD_STATUS = 5;
    public static final int FRM_FIELD_LAST_ENTRY = 6;

    public static String[] fieldNames = {
        "FRM_FIELD_STOCK_PERIODE_ID",
        "FRM_FIELD_PERIODE_TYPE",
        "FRM_FIELD_PERIODE_NAME",
        "FRM_FIELD_START_DATE",
        "FRM_FIELD_END_DATE",
        "FRM_FIELD_STATUS",
        "FRM_FIELD_LAST_ENTRY"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_DATE
    };

    public FrmPeriode() {
    }

    public FrmPeriode(Periode periode) {
        this.periode = periode;
    }

    public FrmPeriode(HttpServletRequest request, Periode periode) {
        super(new FrmPeriode(periode), request);
        this.periode = periode;
    }

    public String getFormName() {
        return FRM_NAME_PERIODE;
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

    public Periode getEntityObject() {
        return periode;
    }

    public void requestEntityObject(Periode periode) {
        try {
            this.requestParam();
            periode.setPeriodeType(getInt(FRM_FIELD_PERIODE_TYPE));
            periode.setPeriodeName(getString(FRM_FIELD_PERIODE_NAME));
            periode.setStartDate(getDate(FRM_FIELD_START_DATE));
            periode.setEndDate(getDate(FRM_FIELD_END_DATE));
            periode.setStatus(getInt(FRM_FIELD_STATUS));
            periode.setLastEntry(getDate(FRM_FIELD_LAST_ENTRY));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
