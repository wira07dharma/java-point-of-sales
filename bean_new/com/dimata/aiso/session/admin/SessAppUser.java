/*
 * SessAppUser.java
 *
 * Created on April 6, 2002, 7:04 AM
 */

package com.dimata.aiso.session.admin;

import java.util.*;
import java.io.*;
import java.sql.*;

import com.dimata.util.*;
import com.dimata.aiso.db.*;

import com.dimata.aiso.form.admin.*;
import com.dimata.aiso.entity.admin.*;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.common.entity.custom.DataCustom;

/**
 *
 * @author  ktanjana
 * @version
 */
public class SessAppUser {

    /** Creates new SessAppUser */
    public SessAppUser() {
    }

    //-------------------Relation AppUser and AppGroup--------------//

    public static Vector getUserGroup(long userID) {
//        System.out.println("---> in : getUserGroup(long userID)");
        PstUserGroup pstUserGroup = new PstUserGroup();
        PstAppGroup pstAppGroup = new PstAppGroup();
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {

            String sql = "SELECT AUG." + PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID] +
                    ", AUG." + PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID] +
                    ", AG." + PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME] +
                    ", AG." + PstAppGroup.fieldNames[PstAppGroup.FLD_DESCRIPTION] +
                    " FROM " + pstUserGroup.getTableName() + " AS AUG ," +
                    pstAppGroup.getTableName() + " AS AG " +
                    "WHERE AUG." + PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID] + " = '" +
                    userID + "'" +
                    " AND AUG." + PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID] + " = " +
                    "AG." + PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_ID];

//            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                AppGroup appGroup = new AppGroup();
                resultToObject(rs, appGroup);
                lists.add(appGroup);
            }
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return new Vector();
    }


    private static void resultToObject(ResultSet rs, AppGroup appGroup) {
        try {
            appGroup.setOID(rs.getLong(PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_ID]));
            appGroup.setGroupName(rs.getString(PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME]));
            appGroup.setDescription(rs.getString(PstAppGroup.fieldNames[PstAppGroup.FLD_DESCRIPTION]));

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }


    // PATERN ## INSERT WITH Vector of  UserGroup
    /**
     * return false if error
     **/
    public static boolean setUserGroup(long userOID, Vector vector) {

        // do delete
        if (PstUserGroup.deleteByUser(userOID) == 0)
            return false;

        if (vector == null || vector.size() == 0)
            return true;

        // than insert
        for (int i = 0; i < vector.size(); i++) {
            UserGroup ug = (UserGroup) vector.get(i);
            if (PstUserGroup.insert(ug) == 0)
                return false;
        }
        return true;
    }

    /**
     *
     * @param userOID
     * @param vector
     * @return
     */
    public static boolean setUserAssignLocation(long userOID, Vector vector) {

        // do delete
        if (PstDataCustom.deleteCustomDataExc(PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID) != 0)
            return false;

        if (vector == null || vector.size() == 0)
            return true;

        // than insert
        for (int i = 0; i < vector.size(); i++) {
            DataCustom dtCustom = new DataCustom();
            dtCustom.setOwnerId(userOID);
            dtCustom.setDataValue((String)vector.get(i));
            try {
                if (PstDataCustom.insertExc(dtCustom) == 0)
                    return false;
            } catch (Exception e) {

            }
        }
        return true;
    }

    
    /**
     *
     * @param userOID
     * @param vDepartment
     * @return 
     */
    public static boolean setUserAssignDepartment(long userOID, Vector vDepartment) 
    {
        String sDepartment = "hrdepartment";
        
        // delete data custom department
        String sWhereClause = PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + " = " + userOID +
                              " AND " + PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] + " = '" + sDepartment + "'";
        int iDeleteResult = PstDataCustom.deleteCustomDataExc(sWhereClause);                      
        if (iDeleteResult != 0)
        {
            return false;
        }

        if ( vDepartment!=null && vDepartment.size()>0 )
        {
            int iDepartmentCount = vDepartment.size();
            for (int i=0; i<iDepartmentCount; i++) 
            {
                DataCustom dtCustom = new DataCustom();
                dtCustom.setOwnerId(userOID);
                dtCustom.setDataName(sDepartment);
                dtCustom.setDataValue((String)vDepartment.get(i));
                try 
                {
                    if (PstDataCustom.insertExc(dtCustom) == 0)
                    {
                        return false;
                    }
                }
                catch (Exception e) 
                {
                    System.out.println("Exc when insert data custom department : " + e.toString());
                }
            }            
        }

        return true;
    }
    
}
