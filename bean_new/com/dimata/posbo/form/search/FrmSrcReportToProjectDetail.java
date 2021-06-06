/**
 * User: wardana
 * Date: Mar 26, 2004
 * Time: 5:38:49 PM
 * Version: 1.0
 */
package com.dimata.posbo.form.search;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.search.SrcReportToProjectDetail;

import javax.servlet.http.HttpServletRequest;

public class FrmSrcReportToProjectDetail extends FRMHandler implements I_FRMInterface, I_FRMType {

    private SrcReportToProjectDetail objSrcRptToPrjDtl;

    public static final String FRM_SRC_RPT_TO_PRJ_DTL = "FRM_SRC_RPT_TO_PRJ_DTL";

    public static final int FRM_FIELD_LOCATION_ID = 0;
    public static final int FRM_FIELD_PROJECT = 1;
    public static final int FRM_FIELD_DATE_FROM = 2;
    public static final int FRM_FIELD_DATE_TO = 3;
    public static final int FRM_FIELD_IGNORE_DATE = 4;
    public static final int FRM_FIELD_PROJECT_NAME = 5;

    public static final String[] stFieldNames = {
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_PROJECT",
        "FRM_FIELD_DATE_FROM",
        "FRM_FIELD_DATE_TO",
        "FRM_FIELD_IGNORE_DATE",
        "FRM_FIELD_PROJECT_NAME"
    };

    public static final int[] iFieldype = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_BOOL,
        TYPE_STRING
    };

    public FrmSrcReportToProjectDetail() {
    }

    public FrmSrcReportToProjectDetail(SrcReportToProjectDetail objSrcRptToPrjDtl) {
        this.objSrcRptToPrjDtl = objSrcRptToPrjDtl;
    }

    public FrmSrcReportToProjectDetail(HttpServletRequest request, SrcReportToProjectDetail objSrcRptToPrjDtl) {
        super(new FrmSrcReportToProjectDetail(objSrcRptToPrjDtl), request);
        this.objSrcRptToPrjDtl = objSrcRptToPrjDtl;
    }


    public int getFieldSize() {
        return stFieldNames.length;
    }

    public String getFormName() {
        return FRM_SRC_RPT_TO_PRJ_DTL;
    }

    public String[] getFieldNames() {
        return stFieldNames;
    }

    public int[] getFieldTypes() {
        return iFieldype;
    }

    public SrcReportToProjectDetail getEntityObject() {
        return this.objSrcRptToPrjDtl;
    }

    public void requestEntityObject(SrcReportToProjectDetail objSrcRptToPrjDtl) {
        try {
            this.requestParam();
            objSrcRptToPrjDtl.setlLocationId(getLong(FRM_FIELD_LOCATION_ID));
            objSrcRptToPrjDtl.setLprojectId(getLong(FRM_FIELD_PROJECT));
            objSrcRptToPrjDtl.setDtDateFrom(getDate(FRM_FIELD_DATE_FROM));
            objSrcRptToPrjDtl.setDtDateTo(getDate(FRM_FIELD_DATE_TO));
            objSrcRptToPrjDtl.setbIgnoreDate(getBoolean(FRM_FIELD_IGNORE_DATE));
            objSrcRptToPrjDtl.setStProjectName(getString(FRM_FIELD_PROJECT_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
