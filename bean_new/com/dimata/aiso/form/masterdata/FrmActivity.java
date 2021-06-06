/*
 * FrmActivity.java
 *
 * Created on January 10, 2007, 5:28 PM
 */

package com.dimata.aiso.form.masterdata;

/* import package servlet */
import javax.servlet.http.HttpServletRequest;

/* import package aiso */
import com.dimata.aiso.entity.masterdata.*;

/* import package qdep */
import com.dimata.qdep.form.*;

/**
 *
 * @author  dwi
 */
public class FrmActivity extends FRMHandler implements I_FRMType, I_FRMInterface {
    
    public static final String FRM_ACTIVITY = "FRM_ACTIVITY";
    
    public static final int FRM_CODE = 0;    
    public static final int FRM_DESCRIPTION = 1;
    public static final int FRM_PARENT_ID = 2;
    public static final int FRM_POSTED = 3;
    public static final int FRM_TYPE = 4;
    public static final int FRM_ASSUMP_AND_RISK = 5;
    public static final int FRM_OUTPUT_AND_DELV = 6;
    public static final int FRM_PERFM_INDICT = 7;
    public static final int FRM_COST_IMPL = 8;
    public static final int FRM_ACT_LEVEL = 9;    
    
    public static String[] fieldNames = {
        "CODE",        
        "DESCRIPTION",
        "PARENT_ID",
        "POSTED",
        "ACTYVITY_TYPE",
        "ASSUMP_AND_RISK",
        "OUTPUT_AND_DELV",
        "FRM_PERFM_INDICT",
        "COST_IMPL",
        "ACT_LEVEL"
    };
    
    public static int[] fieldTypes = {
        
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_LONG,                
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT
    };
    
    private Activity objActivity;
    
    /** Creates a new instance of FrmActivity */
    public FrmActivity(Activity objActivity) {
        this.objActivity = objActivity;
    }
    
     public FrmActivity(HttpServletRequest request, Activity objActivity) {
        super(new FrmActivity(objActivity), request);
        this.objActivity = objActivity;
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
        return FRM_ACTIVITY;
    }
    
    public Activity getEntityObject(){
        return objActivity;
    }
    
     public void requestEntityObject(Activity objActivityx)
    {
        try 
        {
            this.requestParam();
            
            objActivityx.setCode(this.getString(FRM_CODE));        
            objActivityx.setDescription(this.getString(FRM_DESCRIPTION));
            objActivityx.setIdParent(this.getLong(FRM_PARENT_ID));
            objActivityx.setPosted(this.getInt(FRM_POSTED));
            objActivityx.setType(this.getInt(FRM_TYPE));
            objActivityx.setOutPutandDelv(this.getString(FRM_OUTPUT_AND_DELV));
            objActivityx.setPerfmIndict(this.getString(FRM_PERFM_INDICT));
            objActivityx.setAssumpAndRisk(this.getString(FRM_ASSUMP_AND_RISK));
            objActivityx.setCostImpl(this.getString(FRM_COST_IMPL));
            objActivityx.setActLevel(this.getInt(FRM_ACT_LEVEL));
            
            this.objActivity = objActivityx; 
        }
        catch(Exception e) 
        {
            System.out.println("adasd "+e.toString());
            objActivityx = new Activity();
        }       
    }
}
