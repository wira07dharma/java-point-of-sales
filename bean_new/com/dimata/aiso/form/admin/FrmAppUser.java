/*
 * FrmAppUser.java
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
public class FrmAppUser extends FRMHandler implements I_FRMInterface, I_FRMType {

    public static final String FRM_APP_USER = "APP_USER";

    public static final int FRM_LOGIN_ID = 0;
    public static final int FRM_PASSWORD = 1;
    public static final int FRM_CFRM_PASSWORD = 2;
    public static final int FRM_FULL_NAME = 3;
    public static final int FRM_EMAIL = 4;
    public static final int FRM_DESCRIPTION = 5;
    public static final int FRM_REG_DATE = 6;
    public static final int FRM_UPDATE_DATE = 7;
    public static final int FRM_USER_STATUS = 8;
    public static final int FRM_LAST_LOGIN_DATE = 9;
    public static final int FRM_LAST_LOGIN_IP = 10;
    public static final int FRM_USER_GROUP = 11;

    public static final String[] fieldNames = {
        "LOGIN_ID", "PASSWORD", "CFRM_PASSWORD", "FULL_NAME", "EMAIL", "DESCRIPTION"
        , "REG_DATE", "UPDATE_DATE", "USER_STATUS", "LAST_LOGIN_DATE", "LAST_LOGIN_IP"
        , "USER_GROUP"
    };

    public static int[] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + FORMAT_EMAIL, TYPE_STRING,
        TYPE_DATE, TYPE_DATE, TYPE_INT, TYPE_DATE, TYPE_STRING, TYPE_COLLECTION
    };

    private AppUser appUser = new AppUser();


    /** Creates new FrmAppUser */
    public FrmAppUser() {
    }

    public FrmAppUser(HttpServletRequest request) {
        super(new FrmAppUser(), request);
    }

    public String getFormName() {
        return FRM_APP_USER;
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


    public AppUser getEntityObject() {
        return appUser;
    }


    public void requestEntityObject(AppUser entObj) {
        try {
            this.requestParam();
            entObj.setLoginId(this.getString(FRM_LOGIN_ID));
            entObj.setPassword(this.getString(FRM_PASSWORD));
            entObj.setFullName(this.getString(FRM_FULL_NAME));
            entObj.setEmail(this.getString(FRM_EMAIL));
            entObj.setDescription(this.getString(FRM_DESCRIPTION));
            entObj.setRegDate(this.getDate(FRM_REG_DATE));
            entObj.setUpdateDate(this.getDate(FRM_UPDATE_DATE));
            entObj.setUserStatus(this.getInt(FRM_USER_STATUS));
            entObj.setLastLoginDate(this.getDate(FRM_LAST_LOGIN_DATE));
            entObj.setLastLoginIp(this.getString(FRM_LAST_LOGIN_IP));
            entObj.setGroupUser(this.getInt(FRM_USER_GROUP));

            this.appUser = entObj;
        } catch (Exception e) {
            System.out.println("EXC... " + e);
            entObj = new AppUser();
        }
    }

    /**
     * has to be call after requestEntityObject
     * return Vector of UserGroup objects
     **/
    public Vector getUserGroup(long userOID) {
        Vector userGroups = new Vector(1, 1);

        Vector groupOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP]);

        if (groupOIDs == null)
            return userGroups;
        int max = groupOIDs.size();

        for (int i = 0; i < max; i++) {
            long groupOID = ((Long) groupOIDs.get(i)).longValue();
            UserGroup ug = new UserGroup();
            ug.setUserID(userOID);
            ug.setGroupID(groupOID);
            userGroups.add(ug);
        }
        return userGroups;
    }

    /** gadnyana
     * untuk mencari assign lokasi user
     * maksudnya, user ini bisa input untuk
     * transaksi di lokasi mana saja
     * @param userOID
     * @return
     */
    public Vector getUserAssignLocation() {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_DC");

        if (locOIDs == null)
            return userAssigns;
        int max = locOIDs.size();

        for (int i = 0; i < max; i++) {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }
    
    /** edhy
     * untuk mencari assign lokasi user
     * maksudnya, user ini bisa input untuk
     * transaksi di lokasi mana saja
     * @param userOID
     * @return
     */
    public Vector getUserAssignDepartment() 
    {
        Vector userAssigns = new Vector(1, 1);
        Vector locOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP] + "_DEPT");

        if (locOIDs == null)
            return userAssigns;
        
        int max = locOIDs.size();
        for (int i = 0; i < max; i++) 
        {
            long locOID = ((Long) locOIDs.get(i)).longValue();
            userAssigns.add(String.valueOf(locOID));
        }
        return userAssigns;
    }
    
}
