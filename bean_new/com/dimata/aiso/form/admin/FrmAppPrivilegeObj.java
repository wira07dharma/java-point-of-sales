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
public class FrmAppPrivilegeObj extends FRMHandler implements I_FRMInterface, I_FRMType {

    public static final String FRM_APP_PRIVILEGE_OBJ = "APP_PRIVILEGE_OBJ";
        
    public static final int FRM_PRIV_ID 				= 0;
    public static final int FRM_G1_IDX  	 			= 1;
    public static final int FRM_G2_IDX	 			= 2;
    public static final int FRM_OBJ_IDX	 			= 3;
    public static final int FRM_COMMANDS	 			= 4;

    public static String[] fieldNames = {
             "APP_PRIV_ID", "APP_G1_IDX", "APP_G2_IDX", "APP_OBJ_IDX", "APP_COMMANDS"
    } ;

    public static int[] fieldTypes = {
        	TYPE_LONG+ ENTRY_REQUIRED, TYPE_INT, TYPE_INT,
                TYPE_INT, TYPE_INT + TYPE_COLLECTION
    };

    private AppPrivilegeObj appPrivObj = new AppPrivilegeObj();
    
    
    
    /** Creates new FrmAppPriv */
    public FrmAppPrivilegeObj() {
    }

    public FrmAppPrivilegeObj(HttpServletRequest request) {
        super(new FrmAppPrivilegeObj(), request);
    }
    
    public String getFormName() {
        return FRM_APP_PRIVILEGE_OBJ;
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
    
    
    public AppPrivilegeObj getEntityObject(){
        return appPrivObj;
    }
    
        
    public void requestEntityObject(AppPrivilegeObj appPrivObj)
    {        
        try {
            this.requestParam();                    
            
            appPrivObj.setPrivId(this.getLong(FRM_PRIV_ID));
            
            int g1 = this.getInt(FRM_G1_IDX);
            int g2 = this.getInt(FRM_G2_IDX);
            int oidx = this.getInt(FRM_OBJ_IDX);            
            System.out.println(" composeCode("+g1+","+g2+","+oidx+","+AppObjInfo.COMMAND_VIEW);
            int code = AppObjInfo.composeCode(g1,g2, oidx, AppObjInfo.COMMAND_VIEW);            
            System.out.println("code="+code);
            appPrivObj.setCode(code);
            
            Vector cmds = this.getVectorInt(this.fieldNames[FRM_COMMANDS]);
            appPrivObj.setCommands(cmds);
            this.appPrivObj = appPrivObj;
            
        }catch(Exception e) {
            System.out.println("EXC... "+e);
            appPrivObj = new AppPrivilegeObj();
        }       
    }
    
    
}
