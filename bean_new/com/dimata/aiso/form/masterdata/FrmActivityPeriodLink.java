/*
 * FrmActivityPeriodLink.java
 *
 * Created on January 16, 2007, 1:20 PM
 */

package com.dimata.aiso.form.masterdata;

/* import package javax servlet */
import javax.servlet.http.HttpServletRequest;

/* import package aiso */
import com.dimata.aiso.entity.masterdata.*;

/* import package qdep form */
import com.dimata.qdep.form.*;

/**
 *
 * @author  dwi
 */
public class FrmActivityPeriodLink extends FRMHandler implements I_FRMInterface, I_FRMType {
    
    public static final String FRM_ACT_PRD_LINK = "FRM_ACT_PRD_LINK";
    
    public static final int FRM_ACT_PERIOD_ID = 0;
    public static final int FRM_ACTIVITY_ID = 1;
    public static final int FRM_BUDGET = 2;
    
    public static String[] fieldNames = {
        "ACTIVITY_PERIOD_ID",
        "ACTIVITY_ID",
        "BUDGET"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_FLOAT + ENTRY_REQUIRED
    };
    
    private ActivityPeriodLink objActivityPeriodLink;
    
    /** Creates a new instance of FrmActivityPeriodLink */
    public FrmActivityPeriodLink(ActivityPeriodLink objActivityPeriodLink) {
        this.objActivityPeriodLink = objActivityPeriodLink;
    }
    
    public FrmActivityPeriodLink(HttpServletRequest request, ActivityPeriodLink objActivityPeriodLink){
        super(new FrmActivityPeriodLink(objActivityPeriodLink), request);
        this.objActivityPeriodLink = objActivityPeriodLink;
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
        return FRM_ACT_PRD_LINK;
    }
    
    public ActivityPeriodLink getEntityObject(){
        return objActivityPeriodLink;
    }
    
    public void requestEntityObject(ActivityPeriodLink objActivityPeriodLink){
        try{
            this.requestParam();
            
            objActivityPeriodLink.setActivityPeriodId(getLong(FRM_ACT_PERIOD_ID));
            objActivityPeriodLink.setActivityId(getLong(FRM_ACTIVITY_ID));
            objActivityPeriodLink.setBudget(getFloat(FRM_BUDGET));
            
            this.objActivityPeriodLink = objActivityPeriodLink;
        }catch(Exception e){
            System.out.println("Error pd saat request entity object ==> "+e.toString());
            objActivityPeriodLink = new ActivityPeriodLink();
        }
    }
}
