package com.dimata.posbo.form.search;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.search.SrcMatCosting;

import javax.servlet.http.HttpServletRequest;

public class FrmSrcMatCosting extends FRMHandler implements I_FRMInterface, I_FRMType {
    private SrcMatCosting srcMatCosting;

    public static final String FRM_NAME_SRCMATERIALDISPATCHEXC = "FRM_NAME_SRCMATERIALDISPATCHEXC";

    public static final int FRM_FIELD_COSTING_CODE = 0;
    public static final int FRM_FIELD_COSTING_DATE_FROM = 1;
    public static final int FRM_FIELD_COSTING_DATE_TO = 2;
    public static final int FRM_FIELD_IGNORE_DATE = 3;
    public static final int FRM_FIELD_COSTING_FROM = 4;
    public static final int FRM_FIELD_COSTING_TO = 5;
    public static final int FRM_FIELD_STATUS = 6;
    public static final int FRM_FIELD_SORT_BY = 7;

    public static String[] fieldNames =
            {
                "FRM_FIELD_COSTING_CODE", "FRM_FIELD_COSTING_DATE_FROM",
                "FRM_FIELD_COSTING_DATE_TO", "FRM_FIELD_IGNORE_DATE",
                "FRM_FIELD_COSTING_FROM", "FRM_FIELD_COSTING_TO",
                "FRM_FIELD_STATUS", "FRM_FIELD_SORT_BY"
            };

    public static int[] fieldTypes =
            {
                TYPE_STRING, TYPE_DATE,
                TYPE_DATE, TYPE_BOOL,
                TYPE_LONG, TYPE_LONG,
                TYPE_INT, TYPE_INT
            };

    public FrmSrcMatCosting() {
    }

    public FrmSrcMatCosting(SrcMatCosting srcMatCosting) {
        this.srcMatCosting = srcMatCosting;
    }

    public FrmSrcMatCosting(HttpServletRequest request, SrcMatCosting srcMatCosting) {
        super(new FrmSrcMatCosting(srcMatCosting), request);
        this.srcMatCosting = srcMatCosting;
    }

    public String getFormName() {
        return FRM_NAME_SRCMATERIALDISPATCHEXC;
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

    public SrcMatCosting getEntityObject() {
        return srcMatCosting;
    }

    public void requestEntityObject(SrcMatCosting srcMatCosting) {
        try {
            this.requestParam();
            srcMatCosting.setCostingCode(getString(FRM_FIELD_COSTING_CODE));
            srcMatCosting.setCostingDateFrom(getDate(FRM_FIELD_COSTING_DATE_FROM));
            srcMatCosting.setCostingDateTo(getDate(FRM_FIELD_COSTING_DATE_TO));
            srcMatCosting.setIgnoreDate(getBoolean(FRM_FIELD_IGNORE_DATE));
            srcMatCosting.setCostingFrom(getLong(FRM_FIELD_COSTING_FROM));
            srcMatCosting.setCostingTo(getLong(FRM_FIELD_COSTING_TO));
            srcMatCosting.setStatus(getInt(FRM_FIELD_STATUS));
            srcMatCosting.setSortBy(getInt(FRM_FIELD_SORT_BY));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
