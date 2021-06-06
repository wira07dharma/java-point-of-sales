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
public class FrmProvince extends FRMHandler implements I_FRMInterface, I_FRMType {
    public static final String FRM_PROVINCE = "PROVINCE";
    
    public static final int FRM_PROVINCE_ID=0;
    public static final int FRM_PROVINCE_NAME=1;
    
    public static String[] fieldNames =
            {
                "FRM_PROVINCE_ID",
                "FRM_PROVINCE_NAME"
            };
    
    public static int[] fieldTypes =
            {   
                TYPE_LONG,
                TYPE_STRING
            };
    
    private Province province;

    public FrmProvince(Province province) {
        this.province = province;
    }

    public FrmProvince(HttpServletRequest request, Province province) {
        super(new FrmProvince(province), request);
        this.province = province;
    }

    public String getFormName() {
        return FRM_PROVINCE;
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

    public Province getEntityObject() {
        return province;
    }
    
    public void requestEntityObject(Province province) {
        try {
            this.requestParam();
            province.setProvinceName(this.getString(FRM_PROVINCE_NAME));
            this.province = province;
        } catch (Exception e) {
            province = new Province();
        }
    }
    
}
