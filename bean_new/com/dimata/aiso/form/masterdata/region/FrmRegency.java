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
public class FrmRegency extends FRMHandler implements I_FRMInterface, I_FRMType{
    public static final String FRM_REGENCY = "REGENCY";
    
    public static final int FRM_REGENCY_ID=0;
    public static final int FRM_REGENCY_NAME=1;
    
    //Update Tanggal 7 Maret 2013 oleh Hadi
    public static final int FRM_PROVINCE_ID = 2;
    
    public static String[] fieldNames ={
                "FRM_ADDR_REGENCY_ID",
                "FRM_REGENCY_NAME",
                
                //Update Hadi
                "PROVINCE_ID"
    };
    
    public static int[] fieldTypes = {   
                TYPE_LONG,
                TYPE_STRING,
                
                //Update Hadi
                TYPE_LONG
    };
    
    private Regency regency;

    public FrmRegency(Regency regency) {
        this.regency = regency;
    }

    public FrmRegency(HttpServletRequest request, Regency regency) {
        super(new FrmRegency(regency), request);
        this.regency = regency;
    }

    public String getFormName() {
        return FRM_REGENCY;
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

    public Regency getEntityObject() {
        return regency;
    }
    
    public void requestEntityObject(Regency regency) {
        try {
            this.requestParam();
            regency.setRegencyName(this.getString(FRM_REGENCY_NAME));
            
            //Update Hadi
            regency.setIdProvince(this.getLong(FRM_PROVINCE_ID));
            
            this.regency = regency;
        } catch (Exception e) {
            regency = new Regency();
        }
    }
    
}
