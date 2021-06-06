/**
 * User: wardana
 * Date: Mar 25, 2004
 * Time: 3:13:49 PM
 * Version: 1.0
 */
package com.dimata.posbo.form.search;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.search.SrcReportToProjectSummary;

import javax.servlet.http.HttpServletRequest;

public class FrmSrcReportToProjectSummary extends FRMHandler implements I_FRMInterface, I_FRMType {

    private SrcReportToProjectSummary objSrcRptToPrjSumm;

    public static final String FRM_SRC_RPT_TO_PRJ_SUMM = "FRM_SRC_RPT_TO_PRJ_SUMM";

    public static final int FRM_FIELD_LOCATION_ID = 0;
    public static final int FRM_FIELD_DISPATCH_TO = 1;
    public static final int FRM_FIELD_STATUS = 2;
    public static final int FRM_FIELD_DATE_FROM = 3;
    public static final int FRM_FIELD_DATE_TO = 4;

    public static final String[] stFieldNames = {
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_DISPATCH_TO",
        "FRM_FIELD_STATUS",
        "FRM_FIELD_DATE_FROM",
        "FRM_FIELD_DATE_TO"
    };

    public static final int[] iFieldype = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE
    };

    public FrmSrcReportToProjectSummary() {
    }

    public FrmSrcReportToProjectSummary(SrcReportToProjectSummary objSrcRptToPrjSumm) {
        this.objSrcRptToPrjSumm = objSrcRptToPrjSumm;
    }

    public FrmSrcReportToProjectSummary(HttpServletRequest request, SrcReportToProjectSummary objSrcRptToPrjSumm) {
        super(new FrmSrcReportToProjectSummary(objSrcRptToPrjSumm), request);
        this.objSrcRptToPrjSumm = objSrcRptToPrjSumm;
    }

    public int getFieldSize() {
        return stFieldNames.length;
    }

    public String getFormName() {
        return FRM_SRC_RPT_TO_PRJ_SUMM;
    }

    public String[] getFieldNames() {
        return stFieldNames;
    }

    public int[] getFieldTypes() {
        return iFieldype;
    }

    public SrcReportToProjectSummary getEntityObject() {
        return this.objSrcRptToPrjSumm;
    }

    public void requestEntityObject(SrcReportToProjectSummary objSrcRptToPrjSumm) {
        try {
            this.requestParam();
            objSrcRptToPrjSumm.setlLocationId(getLong(FRM_FIELD_LOCATION_ID));
            objSrcRptToPrjSumm.setlDispatchTo(getLong(FRM_FIELD_DISPATCH_TO));
            objSrcRptToPrjSumm.setiStatus(getInt(FRM_FIELD_STATUS));
            objSrcRptToPrjSumm.setDtDateFrom(getDate(FRM_FIELD_DATE_FROM));
            objSrcRptToPrjSumm.setDtDateTo(getDate(FRM_FIELD_DATE_TO));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
