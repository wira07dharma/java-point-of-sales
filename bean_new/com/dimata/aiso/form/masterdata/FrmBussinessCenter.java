/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.form.masterdata;

import com.dimata.aiso.entity.masterdata.BussinessCenter;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author dwi
 */
public class FrmBussinessCenter extends FRMHandler implements I_FRMInterface, I_FRMType{

    public static final String FRM_BUSSINESS_CENTER = "FRM_BUSSINESS_CENTER";      
    
    public static final int FRM_BUSS_CENTER_NAME = 0;
    public static final int FRM_BUSS_GROUP_ID = 1;
    public static final int FRM_CONTACT_ID = 2;
    public static final int FRM_BUSS_CENTER_DESC = 3;

    public static String[] fieldNames =
            {
                "FRM_BUSS_CENTER_NAME",
                "FRM_BUSS_GROUP_ID",
                "FRM_CONTACT_ID",
                "FRM_BUSS_CENTER_DESC"
            };

    public static int[] fieldTypes =
            {
                TYPE_STRING + ENTRY_REQUIRED,
                TYPE_LONG,
                TYPE_LONG,
                TYPE_STRING
            };

    private BussinessCenter objBussinessCenter;

    public FrmBussinessCenter(BussinessCenter objBussinessCenter) {
        this.objBussinessCenter = objBussinessCenter;
    }

    public FrmBussinessCenter(HttpServletRequest request, BussinessCenter objBussinessCenter) {
        super(new FrmBussinessCenter(objBussinessCenter), request);
        this.objBussinessCenter = objBussinessCenter;
    }

    public String getFormName() {
        return FRM_BUSSINESS_CENTER;
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

    public BussinessCenter getEntityObject() {
        return objBussinessCenter;
    }

    public void requestEntityObject(BussinessCenter objBussinessCenter) {
        try {
            this.requestParam();
            objBussinessCenter.setBussCenterName(this.getString(FRM_BUSS_CENTER_NAME));
            objBussinessCenter.setBussGroupId(this.getLong(FRM_BUSS_GROUP_ID));
            objBussinessCenter.setContactId(this.getLong(FRM_CONTACT_ID));
            objBussinessCenter.setBussCenterDesc(this.getString(FRM_BUSS_CENTER_DESC));
            
            this.objBussinessCenter = objBussinessCenter;
        } catch (Exception e) {
            objBussinessCenter = new BussinessCenter();
        }
    }
}
