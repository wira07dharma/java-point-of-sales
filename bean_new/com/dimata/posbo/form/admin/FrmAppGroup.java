/*
 * FrmAppGroup.java
 *
 * Created on April 3, 2002, 10:38 AM
 */

package com.dimata.posbo.form.admin;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.posbo.db.*;
import com.dimata.qdep.form.*;
import com.dimata.posbo.entity.admin.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  ktanjana
 * @version 
 */
public class FrmAppGroup extends FRMHandler implements I_FRMInterface, I_FRMType {

    public static final String FRM_APP_GROUP = "APP_GROUP";
        
    public static final int FRM_GROUP_NAME 				= 0;
    public static final int FRM_REG_DATE	 			= 1;
    public static final int FRM_DESCRIPTION	 			= 2;
    public static final int FRM_GROUP_PRIV	 			= 3;

    public static String[] fieldNames = {
             "APP_GROUP_NAME", "APP_GROUP_REG_DATE", "APP_GROUP_DESCR", "GROUP_PRIV"
    } ;

    public static int[] fieldTypes = {
        	TYPE_STRING + ENTRY_REQUIRED,  TYPE_DATE, TYPE_STRING, TYPE_COLLECTION
    };

    private AppGroup appGroup = new AppGroup();
    
    
    
    /** Creates new FrmAppGroup */
    public FrmAppGroup() {
    }

    public FrmAppGroup(HttpServletRequest request) {
        super(new FrmAppGroup(), request);
    }
    
    public String getFormName() {
        return FRM_APP_GROUP;
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
    
    
    public AppGroup getEntityObject()
    {
        return appGroup;
    }
    
        
    public void requestEntityObject(AppGroup appGroup)
    {        
        try {
            this.requestParam();                    
            appGroup.setGroupName(this.getString(FRM_GROUP_NAME));
            appGroup.setDescription(this.getString(FRM_DESCRIPTION));
            appGroup.setRegDate(this.getDate(FRM_REG_DATE));

            this.appGroup = appGroup;
        }catch(Exception e) {
            System.out.println("EXC... "+e);
            appGroup = new AppGroup();
        }       
    }
    /**
     * has to be call after requestEntityObject
     * return Vector of GroupPriv objects
     **/ 
    public Vector getGroupPriv(long groupOID){
        Vector groupPrivs = new Vector(1,1);
        
        Vector privOIDs = this.getVectorLong(this.fieldNames[FRM_GROUP_PRIV]);        
        
        if (privOIDs==null)
            return groupPrivs;
        int max = privOIDs.size();
        
        for(int i=0; i< max; i++){
            long privOID = ( (Long)privOIDs.get(i)).longValue();
            GroupPriv gp = new GroupPriv();
            gp.setGroupID(groupOID);
            gp.setPrivID(privOID);
            groupPrivs.add(gp);
        }
        return groupPrivs;
    }
    
}
