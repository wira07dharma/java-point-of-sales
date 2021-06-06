/*
 * PstUserGroup.java
 *
 * Created on April 7, 2002, 9:29 AM
 */

package com.dimata.posbo.entity.admin;

/**
 *
 * @author  ktanjana
 * @version
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class PstUserGroup extends DBHandler implements I_DBInterface, I_DBType, I_Persintent  {

    //public static final String TBL_USER_GROUP = "USER_GROUP";
    public static final String TBL_USER_GROUP = "pos_user_group";
    public static final int FLD_USER_ID		= 0;
    public static final int FLD_GROUP_ID                 = 1;

    public static  final String[] fieldNames = {
        "USER_ID", "GROUP_ID"
    } ;

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG
    };


    /** Creates new PstUserGroup */
    public PstUserGroup() {
    }

    public PstUserGroup(int i) throws DBException {
        super(new PstUserGroup());
    }


    public PstUserGroup(String sOid) throws DBException
    {
        super(new PstUserGroup(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstUserGroup(long userOID, long groupOID) throws DBException
    {
        super(new PstUserGroup(0));

        if(!locate(userOID, groupOID))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;

    }


    /**
     *	Implemanting I_Entity interface methods
     */
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_USER_GROUP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstUserGroup().getClass().getName();
    }


    /**
     *	Implementing I_DBInterface interface methods
     */
    public long fetch(Entity ent) {
        UserGroup entObj = PstUserGroup.fetch(ent.getOID(0),ent.getOID(1));
        ent = (Entity)entObj;
        return entObj.getOID();
    }


    public long insert(Entity ent) {
        return PstUserGroup.insert((UserGroup) ent);
    }

    public long update(Entity ent) {
        return update((UserGroup) ent);
    }

    public long delete(Entity ent) {
        return delete((UserGroup) ent);
    }



    public static UserGroup fetch(long userID, long groupID)
    {
        UserGroup entObj = new UserGroup();
        try {
            PstUserGroup pstObj = new PstUserGroup(userID, groupID);
            entObj.setUserID(userID);
            entObj.setGroupID(groupID);
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }


    public static long insert(UserGroup entObj)
    {
        try{
            PstUserGroup pstObj = new PstUserGroup(0);

            pstObj.setLong(FLD_USER_ID, entObj.getUserID());
            pstObj.setLong(FLD_GROUP_ID, entObj.getGroupID());

            pstObj.insert();
            return entObj.getUserID();
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return 0;
    }


    public static long deleteByUser(long oid)
    {
       PstUserGroup pstObj = new PstUserGroup();
       DBResultSet dbrs=null;
       try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID] +
                         " = '" + oid +"'";
            System.out.println(sql);
            int status = DBHandler.execUpdate(sql);
            return oid;
       }catch(Exception e) {
            System.out.println(e);
        }
        finally{
            DBResultSet.close(dbrs);
        }

        return 0;
    }

    public static long deleteByGroup(long oid)
    {
       PstUserGroup pstObj = new PstUserGroup();
       DBResultSet dbrs=null;
       try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID] +
                         " = '" + oid +"'";
            System.out.println(sql);
            int status = DBHandler.execUpdate(sql);
            return oid;
       }catch(Exception e) {
            System.out.println(" PstUserGroup.deleteByPriv "+e);
        }
        finally{
            DBResultSet.close(dbrs);
        }

        return 0;
    }


    public static long update(UserGroup entObj)
    {
        if(entObj != null && entObj.getUserID() != 0)
        {
            try {
                PstUserGroup pstObj = new PstUserGroup(entObj.getUserID(), entObj.getGroupID());

                pstObj.update();
                return entObj.getUserID();
            }catch(Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }


    public static Vector listAll()
    {
        return list(0, 0, null,null);
    }


    public static Vector list(int limitStart, int recordToGet, String whereClause, String order)
    {
       return new Vector();
    }



   public static JSONObject fetchJSON(long userID, long groupID){
      JSONObject object = new JSONObject();
      try {
         GroupPriv groupPriv = PstGroupPriv.fetch(userID, groupID);
         object.put(PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID], groupPriv.getGroupID());
         object.put(PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID], groupPriv.getPrivID());
      }catch(Exception exc){}
      return object;
   }
    
}
