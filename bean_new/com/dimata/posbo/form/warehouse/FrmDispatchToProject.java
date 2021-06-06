/**
 * User: wardana
 * Date: Mar 23, 2004
 * Time: 3:34:37 PM
 * Version: 1.0
 */
package com.dimata.posbo.form.warehouse;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.posbo.entity.warehouse.DispatchToProject;

import javax.servlet.http.HttpServletRequest;

public class FrmDispatchToProject extends FRMHandler implements I_FRMInterface, I_FRMType {

    private DispatchToProject objDspToProject;

    public static final String FRM_NAME_DISPATCH_TO_PROJECT = "DISPATCH_TO_PROJECT";

    public static final int FRM_FLD_DISPATCH_MATERIAL_ID = 0;
    public static final int FRM_FLD_LOCATION_ID = 1;
    public static final int FRM_FLD_PROJECT_ID = 2;
    public static final int FRM_FLD_LOCATION_TYPE = 3;
    public static final int FRM_FLD_DISPATCH_DATE = 4;
    public static final int FRM_FLD_DISPATCH_CODE = 5;
    public static final int FRM_FLD_DISPATCH_STATUS = 6;
    public static final int FRM_FLD_REMARK = 7;
    public static final int FRM_FLD_DISPATCH_CODE_COUNTER = 8;
    public static final int FRM_FLD_DISPATCH_TO = 9;
    public static final int FRM_FLD_INVOICE_SUPPLIER = 10;

    public static final String[] stFieldNames = {
        "FRM_FIELD_DISPATCH_MATERIAL_ID",
        "FRM_FIELD_LOCATION_ID",
        "FRM_FIELD_PROJECT_ID",
        "FRM_FIELD_LOCATION_TYPE",
        "FRM_FIELD_DISPATCH_DATE",
        "FRM_FIELD_DISPATCH_CODE",
        "FRM_FIELD_DISPATCH_STATUS",
        "FRM_FIELD_REMARK",
        "FRM_FIELD_DISPATCH_CODE_COUNTER",
        "FRM_FIELD_DISPATCH_TO",
        "FRM_FIELD_INVOICE_SUPPLIER"
    };

    public static final int[] iFieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
    };

    public FrmDispatchToProject() {
    }

    public FrmDispatchToProject(DispatchToProject objDspToProject) {
        this.objDspToProject = objDspToProject;
    }

    public FrmDispatchToProject(HttpServletRequest request, DispatchToProject objDspToProject) {
        super(new FrmDispatchToProject(objDspToProject), request);
        this.objDspToProject = objDspToProject;
    }


    public int getFieldSize() {
        return stFieldNames.length;
    }

    public String getFormName() {
        return FRM_NAME_DISPATCH_TO_PROJECT;
    }

    public String[] getFieldNames() {
        return stFieldNames;
    }

    public int[] getFieldTypes() {
        return iFieldTypes;
    }

    public DispatchToProject getEntityObject(){
        return objDspToProject;
    }

    public void requestEntityObject(DispatchToProject objDspToProject){
        try{
            this.requestParam();
            objDspToProject.setlLocationId(getLong(FRM_FLD_LOCATION_ID));
            objDspToProject.setlProjectId(getLong(FRM_FLD_PROJECT_ID));
            objDspToProject.setiLocationType(getInt(FRM_FLD_LOCATION_TYPE));
            objDspToProject.setDtDispatchDate(getDate(FRM_FLD_DISPATCH_DATE));
            objDspToProject.setStDispatchCode(getString(FRM_FLD_DISPATCH_CODE));
            objDspToProject.setiDispatchStatus(getInt(FRM_FLD_DISPATCH_STATUS));
            objDspToProject.setStRemark(getString(FRM_FLD_REMARK));
            objDspToProject.setiDispatchCodeCounter(getInt(FRM_FLD_DISPATCH_CODE_COUNTER));
            objDspToProject.setlDispatchTo(getLong(FRM_FLD_DISPATCH_TO));
            objDspToProject.setStInvoiceSupplier(getString(FRM_FLD_INVOICE_SUPPLIER));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
