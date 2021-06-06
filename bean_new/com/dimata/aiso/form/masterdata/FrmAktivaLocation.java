/*
 * FrmAktivaLocation.java
 *
 * Created on February 25, 2008, 5:56 PM
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.*;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author  DWI
 */
public class FrmAktivaLocation extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_AKTIVA_LOCATION = "FORM_AKTIVA_LOCATION";
    public static final int FRM_AKTIVA_LOC_CODE = 0;
    public static final int FRM_AKTIVA_LOC_NAME = 1;

    public static String[] fieldNames =
            {
                "FRM_AKTIVA_LOC_CODE",
                "FRM_AKTIVA_LOC_NAME"
            };

    public static int[] fieldTypes =
            {
                TYPE_STRING + ENTRY_REQUIRED,
                TYPE_STRING + ENTRY_REQUIRED,
            };

    private AktivaLocation objAktivaLocation;

    public FrmAktivaLocation(AktivaLocation objAktivaLocation) {
        this.objAktivaLocation = objAktivaLocation;
    }

    public FrmAktivaLocation(HttpServletRequest request, AktivaLocation objAktivaLocation) {
        super(new FrmAktivaLocation(objAktivaLocation), request);
        this.objAktivaLocation = objAktivaLocation;
    }

    public String getFormName() {
        return FRM_AKTIVA_LOCATION;
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

    public AktivaLocation getEntityObject() {
        return objAktivaLocation;
    }

    public void requestEntityObject(AktivaLocation objAktivaLocation) {
        try {
            this.requestParam();
            objAktivaLocation.setAktivaLocCode(this.getString(FRM_AKTIVA_LOC_CODE));
            objAktivaLocation.setAktivaLocName(this.getString(FRM_AKTIVA_LOC_NAME));
            this.objAktivaLocation = objAktivaLocation;
        } catch (Exception e) {
            objAktivaLocation = new AktivaLocation();
        }
    }
    
}
