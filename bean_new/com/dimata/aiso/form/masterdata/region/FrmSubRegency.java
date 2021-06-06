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
public class FrmSubRegency extends FRMHandler implements I_FRMInterface, I_FRMType {
    public static final String FRM_SUBREGENCY = "SUB_REGENCY";
    
    public static final int FRM_SUBREGENCY_ID=0;
    public static final int FRM_SUBREGENCY_NAME=1;
    
    //Update tanggal 7 Maret 2013 oleh Hadi
    public static final int FRM_REGENCY_ID = 2;
    
    public static String[] fieldNames ={
                "FRM_ADDR_SUBREGENCY_ID",
                "FRM_SUBREGENCY_NAME",
                
                //Update Hadi
                "FRM_REGENCY_ID"
    };
    
    public static int[] fieldTypes ={   
                TYPE_LONG,
                TYPE_STRING,
                
                //Update Hadi
                TYPE_LONG
    };
    
    private SubRegency subRegency;

    public FrmSubRegency(SubRegency subRegency) {
        this.subRegency = subRegency;
    }

    public FrmSubRegency(HttpServletRequest request, SubRegency subregency) {
        super(new FrmSubRegency(subregency), request);
        this.subRegency = subRegency;
    }

    public String getFormName() {
        return FRM_SUBREGENCY;
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

    public SubRegency getEntityObject() {
        return subRegency;
    }
    
    public void requestEntityObject(SubRegency subRegency) {
        try {
            this.requestParam();
            subRegency.setSubRegencyName(this.getString(FRM_SUBREGENCY_NAME));
            
            //Update
            subRegency.setIdRegency(this.getLong(FRM_REGENCY_ID));
            
            this.subRegency = subRegency;
        } catch (Exception e) {
            subRegency = new SubRegency();
        }
    }
    
}
