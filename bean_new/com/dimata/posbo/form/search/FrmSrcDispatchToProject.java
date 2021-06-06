/**
 * User: wardana
 * Date: Mar 25, 2004
 * Time: 9:03:42 AM
 * Version: 1.0
 */
package com.dimata.posbo.form.search;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.search.SrcDispatchToProject;

import javax.servlet.http.HttpServletRequest;

public class FrmSrcDispatchToProject extends FRMHandler implements I_FRMInterface, I_FRMType {

    private SrcDispatchToProject objSrcDispatchToProject;

    public static final String FRM_NAME_SRC_DSP_TO_PROJECT = "FRM_NAME_SRC_DSP_TO_PROJECT";

    public static final int FRM_FIELD_DISPATCH_CODE = 0;
    public static final int FRM_FIELD_DISPATCH_DATE_FROM = 1;
    public static final int FRM_FIELD_DISPATCH_DATE_TO = 2;
    public static final int FRM_FIELD_IGNORE_DATE = 3;
    public static final int FRM_FIELD_DISPATCH_FROM = 4;
    public static final int FRM_FIELD_DISPATCH_TO = 5;
    public static final int FRM_FIELD_STATUS = 6;
    public static final int FRM_FIELD_SORT_BY = 7;

    public static String[] stFieldNames =
            {
                "FRM_FIELD_DISPATCH_CODE", "FRM_FIELD_DISPATCH_DATE_FROM",
                "FRM_FIELD_DISPATCH_DATE_TO", "FRM_FIELD_IGNORE_DATE",
                "FRM_FIELD_DISPATCH_FROM", "FRM_FIELD_DISPATCH_TO",
                "FRM_FIELD_STATUS", "FRM_FIELD_SORT_BY"
            };

    public static int[] iFieldTypes =
            {
                TYPE_STRING,
                TYPE_DATE,
                TYPE_DATE,
                TYPE_BOOL,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_INT
            };

    public FrmSrcDispatchToProject() {
    }

    public FrmSrcDispatchToProject(SrcDispatchToProject objSrcDspToPrj) {
        this.objSrcDispatchToProject = objSrcDspToPrj;
    }

    public FrmSrcDispatchToProject(HttpServletRequest request, SrcDispatchToProject objSrcDspToProject) {
        super(new FrmSrcDispatchToProject(objSrcDspToProject), request);
        this.objSrcDispatchToProject = objSrcDspToProject;
    }


    public int getFieldSize() {
        return stFieldNames.length;
    }

    public String getFormName() {
        return FRM_NAME_SRC_DSP_TO_PROJECT;
    }

    public String[] getFieldNames() {
        return stFieldNames;
    }

    public int[] getFieldTypes() {
        return iFieldTypes;
    }

    public SrcDispatchToProject getEntityObject(){
        return objSrcDispatchToProject;
    }

    public void requestEntityObject(SrcDispatchToProject objSrcDspToProject){
        try{
            this.requestParam();
            objSrcDspToProject.setStDispatchCode(getString(FRM_FIELD_DISPATCH_CODE));
            objSrcDspToProject.setDtDispatchDateFrom(getDate(FRM_FIELD_DISPATCH_DATE_FROM));
            objSrcDspToProject.setDtDispatchDateTo(getDate(FRM_FIELD_DISPATCH_DATE_TO));
            objSrcDspToProject.setbIgnoreDate(getBoolean(FRM_FIELD_IGNORE_DATE));
            objSrcDspToProject.setlDispatchFrom(getLong(FRM_FIELD_DISPATCH_FROM));
            objSrcDspToProject.setlDispatchTo(getLong(FRM_FIELD_DISPATCH_TO));
            objSrcDspToProject.setiStatus(getInt(FRM_FIELD_STATUS));
            objSrcDspToProject.setiSortBy(getInt(FRM_FIELD_SORT_BY));

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
