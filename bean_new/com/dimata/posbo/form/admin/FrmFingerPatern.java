package com.dimata.posbo.form.admin;

import com.dimata.posbo.entity.admin.*;
import com.dimata.qdep.form.*;
import java.util.*;
import javax.servlet.http.*;

public class FrmFingerPatern 
extends FRMHandler 
implements I_FRMInterface, I_FRMType {
    
    public static final String FRM_FINGER_PATERN = "FINGER_PATERN";
    
    public static final int FRM_FINGER_PATERN_ID	= 0;
    public static final int FRM_EMPLOYEE_ID		= 1;
    public static final int FRM_FINGER_TYPE		= 2;
    public static final int FRM_FINGER_PATERN_DATA      = 3;
    
    private FingerPatern fingerPatern = new FingerPatern();
    
    public static  final String[] fieldNames = {
        "FINGER_PATERN_ID",
        "EMPLOYEE_ID",
        "FINGER_TYPE",
        "FINGER_PATERN_DATA"
    };
    
     public static int[] fieldTypes = {
        TYPE_LONG + ENTRY_REQUIRED, 
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_INT + ENTRY_REQUIRED,
        TYPE_STRING
    };
     
    
     
    public FrmFingerPatern() {
    }

    public FrmFingerPatern(FingerPatern fingerPatern){
        this.fingerPatern = fingerPatern;
    }
    public FrmFingerPatern(HttpServletRequest request, FingerPatern fingerPatern){
        super(new FrmFingerPatern(fingerPatern),request);// super ini 
        this.fingerPatern = fingerPatern;
    }

    @Override
    public int getFieldSize() {
        return fieldNames.length;
    }

    @Override
    public String getFormName() {
        return FRM_FINGER_PATERN;
    }

    @Override
    public String[] getFieldNames() {
        return fieldNames;
    }

    @Override
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public FingerPatern getEntityObject()
    {
        return fingerPatern;
    }
    
    public void requestEntityObject(FingerPatern fingerPatern)
    {        
        try{
            this.requestParam();
            fingerPatern.setEmployeeId(getLong(FRM_EMPLOYEE_ID)); 
            fingerPatern.setFingerType(getInt(FRM_FINGER_TYPE));
            fingerPatern.setFingerPatern(getString(FRM_FINGER_PATERN_DATA));
        }catch(Exception e){
            System.out.println("Error on requestEntityObject : "+e.toString());
        }
    }
    
}
