/*
 * FrmActivityPeriod.java
 *
 * Created on January 5, 2007, 9:49 AM
 */

package com.dimata.aiso.form.periode;

/* package servlet*/
import javax.servlet.http.*;
import javax.servlet.*;

/* package qdep*/
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType; 

/* package aiso*/
import com.dimata.aiso.entity.periode.*;

/**
 *
 * @author  dwi
 */
public class FrmActivityPeriod extends FRMHandler implements I_FRMInterface, I_FRMType {
    public static final String FRM_ACTIVITY_PERIOD = "PERIODE";

    public static final int FRM_START_DATE         = 0;
    public static final int FRM_END_DATE           = 1;
    public static final int FRM_NAME               = 2;
    public static final int FRM_DESCRIPTION        = 3;
    public static final int FRM_POSTED             = 4;    

    public static String[] fieldNames = {
        "START_DATE",
        "END_DATE",
        "NAME",
        "DESCRIPTION",
        "POSTED"        
    };
    
    public static int[] fieldTypes = {
        TYPE_DATE,
        TYPE_DATE,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_INT        
    };

    private ActivityPeriod actPeriod;

    public FrmActivityPeriod(ActivityPeriod actPeriod){
        this.actPeriod = actPeriod;
    }
    
    public FrmActivityPeriod(HttpServletRequest request, ActivityPeriod actPeriod) {
        super(new FrmActivityPeriod(actPeriod), request);
        this.actPeriod = actPeriod;
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
        return FRM_ACTIVITY_PERIOD;
    }
    
     public ActivityPeriod getEntityObject() { 
        return actPeriod;
    }

    public void requestEntityObject(ActivityPeriod actPeriod) {        
        try {
            
            this.requestParam();
            actPeriod.setStartDate(this.getDate(FRM_START_DATE));
            actPeriod.setEndDate(this.getDate(FRM_END_DATE));
            actPeriod.setName(this.getString(FRM_NAME));
            actPeriod.setDescription(this.getString(FRM_DESCRIPTION));
            //actPeriod.setPosted(this.getInt(FRM_POSTED));    
            actPeriod.setPosted(PstActivityPeriod.PERIOD_OPEN);
            this.actPeriod = actPeriod;
        } catch(Exception e) {            
            actPeriod = new ActivityPeriod();
        }       
    }
    
}
