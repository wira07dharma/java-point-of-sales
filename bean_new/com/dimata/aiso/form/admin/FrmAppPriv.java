/*
 * FrmAppPriv.java
 *
 * Created on April 3, 2002, 10:38 AM
 */

package com.dimata.aiso.form.admin;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.aiso.db.*;
import com.dimata.qdep.form.*;
import com.dimata.aiso.entity.admin.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  ktanjana
 * @version 
 */
public class FrmAppPriv extends FRMHandler implements I_FRMInterface, I_FRMType {

    public static final String FRM_APP_PRIVILEGE = "APP_PRIVILEGE";
        
    public static final int FRM_PRIV_NAME 				= 0;
    public static final int FRM_REG_DATE	 			= 1;
    public static final int FRM_DESCRIPTION	 			= 2;

    public static String[] fieldNames = {
             "APP_PRIV_NAME", "APP_PRIV_REG_DATE", "APP_PRIV_DESCR"
    } ;

    public static int[] fieldTypes = {
        	TYPE_STRING + ENTRY_REQUIRED,  TYPE_DATE, TYPE_STRING
    };

    private AppPriv appPriv = new AppPriv();
    
    
    
    /** Creates new FrmAppPriv */
    public FrmAppPriv() {
    }

    public FrmAppPriv(HttpServletRequest request) {
        super(new FrmAppPriv(), request);
    }
    
    public String getFormName() {
        return FRM_APP_PRIVILEGE;
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
    
    
    public AppPriv getEntityObject()
    {
        return appPriv;
    }
    
        
    public void requestEntityObject(AppPriv appPriv)
    {        
        try {
            this.requestParam();                    
            appPriv.setPrivName(this.getString(FRM_PRIV_NAME));
            appPriv.setDescr(this.getString(FRM_DESCRIPTION));
            appPriv.setRegDate(this.getDate(FRM_REG_DATE));

            this.appPriv = appPriv;
        }catch(Exception e) {
            System.out.println("EXC... "+e);
            appPriv = new AppPriv();
        }       
    }
    
    
}
