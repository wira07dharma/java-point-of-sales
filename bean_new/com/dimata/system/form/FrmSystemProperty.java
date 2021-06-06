/*
 * FrmSystemProperty.java
 *
 * Created on March 13, 2002, 10:34 AM
 */

/**
 *
 * @author  gmudiasa
 * @version 
 */
 

package com.dimata.system.form;

import javax.servlet.*;
import javax.servlet.http.*;

import com.dimata.qdep.form.*;
import com.dimata.qdep.entity.*;

import com.dimata.system.entity.*;


public class FrmSystemProperty extends FRMHandler implements I_FRMInterface, I_FRMType
{

    public static final String FRM_SYSPROP = "SYSPROP";
        
    public static final int FRM_NAME 	= 0;
    public static final int FRM_VALUE 	= 1;
    public static final int FRM_VALTYPE	= 2;
    public static final int FRM_DISTYPE	= 3;
    public static final int FRM_GROUP	= 4;
    public static final int FRM_NOTE	= 5;
    
    
    public static String[] fieldNames = {
        "NAME",
        "VALUE",
        "VALTYPE",
        "DISTYPE",
        "GROUP",
        "NOTE"
    };
    
    public static int[] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING  + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING
    };    
    

    private SystemProperty sysProp;
    
    
    
    /** Creates new FrmSystemProperty */ 
    public FrmSystemProperty(SystemProperty sysProp)     
    { 
        this.sysProp = sysProp;        
    }  
        
    
    public FrmSystemProperty(HttpServletRequest request, SystemProperty sysProp) {
        super(new FrmSystemProperty(sysProp), request);
        this.sysProp = sysProp; 
    }

    public String getFormName() {
        return FRM_SYSPROP;
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
       
    
    

    public SystemProperty getEntityObject()
    {
        return sysProp;
    }
    
    public SystemProperty setEntityObject(SystemProperty sp)
    {
        return sysProp = sp;
    }
    
    
    public void requestEntityObject(SystemProperty sysProp)
    {        
        try {
            this.requestParam();                    
            sysProp.setName(this.getString(FRM_NAME));
            sysProp.setValue(this.getString(FRM_VALUE));
            sysProp.setValueType(this.getString(FRM_VALTYPE));
            sysProp.setDisplayType(this.getString(FRM_DISTYPE));
            sysProp.setGroup(this.getString(FRM_GROUP));
            sysProp.setNote(this.getString(FRM_NOTE));
            
            this.sysProp = sysProp;
        }catch(Exception e) { 
            System.out.println("***FrmSystemProperty.requestEntityObject() " + e.toString());
            sysProp = new SystemProperty();
        }       
    }

    
} 
