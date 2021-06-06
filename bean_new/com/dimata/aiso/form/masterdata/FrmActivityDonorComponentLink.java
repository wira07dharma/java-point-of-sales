/*
 * FrmActivityDonorComponentLink.java
 *
 * Created on January 23, 2007, 5:35 PM
 */

package com.dimata.aiso.form.masterdata;

/* import package javax servlet */
import javax.servlet.http.HttpServletRequest;

/* import package aiso */
import com.dimata.aiso.entity.masterdata.*;

/* import package qdep */
import com.dimata.qdep.form.*;

/**
 *
 * @author  dwi
 */
public class FrmActivityDonorComponentLink extends FRMHandler implements I_FRMInterface, I_FRMType{
    
    public static final String FRM_ACTIVITY_ASSIGN = "FRM_ACTIVITY_ASSIGN";
    
    public static final int FRM_ACTIVITY_ID = 0;
    public static final int FRM_ACTIVITY_PERIOD_ID = 1;
    public static final int FRM_DONOR_COMPONENT_ID = 2;
    public static final int FRM_SHARE_BUDGET = 3;
    public static final int FRM_SHARE_PROCENTAGE = 4;
    
    public static String[] fieldNames = {
        "ACTIVITY_ID",
        "ACTIVITY_PERIOD_ID",
        "DONOR_COMPONENT_ID",
        "SHARE_BUDGET",
        "SHARE_PROCENTAGE"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_FLOAT + ENTRY_REQUIRED,
        TYPE_FLOAT + ENTRY_REQUIRED
    };
    
    private ActivityDonorComponentLink objActivityDonorComponentLink;
    
    /** Creates a new instance of FrmActivityDonorComponentLink */
    public FrmActivityDonorComponentLink(ActivityDonorComponentLink objActivityDonorComponentLink) {
        this.objActivityDonorComponentLink = objActivityDonorComponentLink;
    }
    
    public FrmActivityDonorComponentLink(HttpServletRequest request, ActivityDonorComponentLink objActivityDonorComponentLink){
        super(new FrmActivityDonorComponentLink(objActivityDonorComponentLink), request);
        this.objActivityDonorComponentLink = objActivityDonorComponentLink;
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
        return FRM_ACTIVITY_ASSIGN;
    }
    
    public ActivityDonorComponentLink getEntityObject(){
        return objActivityDonorComponentLink;
    }
    
    public void requestEntityObject(ActivityDonorComponentLink objActivityDonorComponentLink){
        try{
            this.requestParam();
            
            objActivityDonorComponentLink.setActivityId(this.getLong(FRM_ACTIVITY_ID));
            objActivityDonorComponentLink.setActivityPeriodId(this.getLong(FRM_ACTIVITY_PERIOD_ID));
            objActivityDonorComponentLink.setDonorComponentId(this.getLong(FRM_DONOR_COMPONENT_ID));
            objActivityDonorComponentLink.setShareBudget(this.getFloat(FRM_SHARE_BUDGET));
            objActivityDonorComponentLink.setShareProcentage(this.getFloat(FRM_SHARE_PROCENTAGE));
            
            this.objActivityDonorComponentLink = objActivityDonorComponentLink;
        }catch(Exception e){
            System.out.println("Error pd method requestEntityObject ==> "+e.toString());
            objActivityDonorComponentLink = new ActivityDonorComponentLink();
        }
    }
}
