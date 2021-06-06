/*
 * FrmDonorComponent.java
 *
 * Created on January 9, 2007, 2:39 PM
 */

package com.dimata.aiso.form.masterdata;

/* import package aiso*/
import com.dimata.aiso.entity.masterdata.DonorComponent;

/* import package qdep */
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.qdep.form.I_FRMInterface;

/* import package servlet http */
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author  dwi
 */
public class FrmDonorComponent extends FRMHandler implements I_FRMType,  I_FRMInterface {
    public static final String FRM_DONOR_COMPONENT = "FRM_DONOR_COMPONENT";
    public static final int FRM_CODE = 0;
    public static final int FRM_NAME = 1;
    public static final int FRM_DESCRIPTION = 2;
    public static final int FRM_CONTACT_ID = 3;
    
    public static String [] fieldNames = {
        "CODE",
        "NAME",
        "DESCRIPTION",
        "CONTACT_ID"
    };
    
    public static int [] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_LONG + ENTRY_REQUIRED
    };
    
    private DonorComponent objDonorComponent;
    
    /** Creates a new instance of FrmDonorComponent */
    public FrmDonorComponent(DonorComponent objDonorComponent) {
        this.objDonorComponent = objDonorComponent;
    }
    
    public FrmDonorComponent(HttpServletRequest request, DonorComponent objDonorComponent) {
        super(new FrmDonorComponent(objDonorComponent), request);
        this.objDonorComponent = objDonorComponent;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getFormName() {
        return FRM_DONOR_COMPONENT;
    }
    
    public DonorComponent getEntityObject() {
        return objDonorComponent;
    }

    public void requestEntityObject(DonorComponent objDonorComponent) {
        try {           
            this.requestParam();            
            objDonorComponent.setCode(this.getString(FRM_CODE));
            objDonorComponent.setName(this.getString(FRM_NAME));
            objDonorComponent.setDescription(this.getString(FRM_DESCRIPTION));
            objDonorComponent.setContactId(this.getLong(FRM_CONTACT_ID));

            this.objDonorComponent = objDonorComponent;
        } catch (Exception e) {
            objDonorComponent = new DonorComponent();
        }
    }
    
}
