/*
 * FrmActivityAccountLink.java
 *
 * Created on January 13, 2007, 11:12 AM
 */

package com.dimata.aiso.form.masterdata;

/* import package http servle */
import javax.servlet.http.HttpServletRequest;

/* import package aiso */
import com.dimata.aiso.entity.masterdata.*;

/* import package qdep */
import com.dimata.qdep.form.*;

/**
 *
 * @author  dwi
 */
public class FrmActivityAccountLink extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    public static final String FRM_ACT_ACC_LINK = "FRM_ACT_ACC_LINK";
    
    public static final int FRM_ACCOUNT_ID = 0;
    public static final int FRM_ACTIVITY_ID = 1;
    
    public static String[] fieldNames = {
        "ACCOUNT_ID",
        "ACTIVITY_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG
    };
    
    private ActivityAccountLink objActivityAccountLink;
    
    /** Creates a new instance of FrmActivityAccountLink */
    public FrmActivityAccountLink(ActivityAccountLink objActivityAccountLink) {
        this.objActivityAccountLink = objActivityAccountLink;
    }
    
    public FrmActivityAccountLink(HttpServletRequest request, ActivityAccountLink objActivityAccountLink){
        super(new FrmActivityAccountLink(objActivityAccountLink), request);
        this.objActivityAccountLink = objActivityAccountLink;    
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
        return FRM_ACT_ACC_LINK;
    }
    
    public ActivityAccountLink getEntityObject(){
        return objActivityAccountLink;
    }
    
    public void requestEntityObject(ActivityAccountLink objActivityAccountLink){
        try{
            this.requestParam();
            
            objActivityAccountLink.setIdPerkiraan(this.getLong(FRM_ACCOUNT_ID));
            objActivityAccountLink.setActivityId(this.getLong(FRM_ACTIVITY_ID));
            
            this.objActivityAccountLink = objActivityAccountLink;
        
        }catch(Exception e){
            System.out.println("Error pd saat request entity object ==> "+e.toString());
            objActivityAccountLink = new ActivityAccountLink();
        }
    
    }
}
