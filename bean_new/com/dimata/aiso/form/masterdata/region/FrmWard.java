/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.form.masterdata.region;

import com.dimata.aiso.entity.masterdata.region.*;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dw1p4
 */
public class FrmWard extends FRMHandler implements I_FRMInterface, I_FRMType {
    public static final String FRM_WARD = "WARD";
    
    public static final int FRM_WARD_ID = 0;
    public static final int FRM_WARD_NAME = 1;
    
    //update tanggal 7 Maret 2013 oleh Hadi
    public static final int FRM_WARD_SUBREGENCY_ID = 2;
    public static final int FRM_WARD_PROVINCE_ID = 3;
    public static final int FRM_WARD_CITY_ID = 4;
    public static final int FRM_WARD_REGENCY_ID = 5;
    
    public static String[] fieldNames ={
        "FRM_WARD_ID",
        "FRM_WARD_NAME",
            
            //UPDATE hadi
            "SUBREGENCY_ID",
            "PROVINCE_ID",
            "CITY_ID",
            "REGENCY_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
                
                //update hadi
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_LONG
    };
    
    private Ward ward;

    public FrmWard(Ward ward) {
        this.ward = ward;
    }

    public FrmWard(HttpServletRequest request, Ward ward) {
        super(new FrmWard(ward), request);
        this.ward = ward;
    }

    public String getFormName() {
        return FRM_WARD;
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

    public Ward getEntityObject() {
        return ward;
    }
    
    public void requestEntityObject(Ward ward) {
        try {
            this.requestParam();
            ward.setWardName(this.getString(FRM_WARD_NAME));
            
            ward.setIdSubRegency(this.getLong(FRM_WARD_SUBREGENCY_ID));
            
            this.ward = ward;
        } catch (Exception e) {
            ward = new Ward();
        }
    }
    
}
