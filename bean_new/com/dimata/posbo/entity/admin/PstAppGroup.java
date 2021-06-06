/*
 * PstAppGroup.java
 *
 * Created on April 3, 2002, 9:29 AM
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

public class PstAppGroup extends DBHandler implements I_DBInterface, I_DBType, I_Persintent  {

    //public static final String TBL_APP_GROUP = "APP_GROUP";
    public static final String TBL_APP_GROUP = "pos_app_group";
    public static final int FLD_GROUP_ID		= 0;
    public static final int FLD_GROUP_NAME		= 1;
    public static final int FLD_REG_DATE 		= 2;
    public static final int FLD_DESCRIPTION 		= 3;


    public static  final String[] fieldNames = {
        "GROUP_ID",
        "GROUP_NAME",
        "REG_DATE",
        "DESCRIPTION"
    } ;

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID, TYPE_STRING,  TYPE_DATE, TYPE_STRING
    };

    /** Creates new PstAppGroup */
    public PstAppGroup() {
    }

    public PstAppGroup(int i) throws DBException {
        super(new PstAppGroup());
    }


    public PstAppGroup(String sOid) throws DBException {
        super(new PstAppGroup(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstAppGroup(long lOid) throws DBException {
        super(new PstAppGroup(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }

        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;

    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_APP_GROUP;
    }

    public String getPersistentName() {
        return new PstAppGroup().getClass().getName();
    }


    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }


    public long delete(Entity ent) {
        return delete((AppGroup) ent);
    }

    public long insert(Entity ent) {
        return PstAppGroup.insert((AppGroup) ent);
    }


    public long update(Entity ent) {
        return update((AppGroup) ent);
    }

    public long fetch(Entity ent) {
        AppGroup entObj = PstAppGroup.fetch(ent.getOID());
        ent = (Entity)entObj;
        return ent.getOID();
    }


    public static AppGroup fetch(long oid) {
        AppGroup entObj = new AppGroup();
        try {
            PstAppGroup pstObj = new PstAppGroup(oid);
            entObj.setOID(oid);
            entObj.setGroupName(pstObj.getString(FLD_GROUP_NAME));
            entObj.setDescription(pstObj.getString(FLD_DESCRIPTION));
            entObj.setRegDate(pstObj.getDate(FLD_REG_DATE));
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }


    public static long insert(AppGroup entObj) {
        try{
            PstAppGroup pstObj = new PstAppGroup(0);
            pstObj.setString(FLD_GROUP_NAME, entObj.getGroupName());
            pstObj.setDate(FLD_REG_DATE, entObj.getRegDate());
            pstObj.setString(FLD_DESCRIPTION, entObj.getDescription());

            pstObj.insert();
            entObj.setOID(pstObj.getlong(FLD_GROUP_ID));
            return entObj.getOID();
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return 0;
    }


    public static long update(AppGroup entObj) {
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstAppGroup pstObj = new PstAppGroup(entObj.getOID());
                pstObj.setString(FLD_GROUP_NAME, entObj.getGroupName());
                pstObj.setDate(FLD_REG_DATE, entObj.getRegDate());
                pstObj.setString(FLD_DESCRIPTION, entObj.getDescription());

                pstObj.update();
                return entObj.getOID();
            }catch(Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }


    public static long delete(long oid) {
        try {
            PstAppGroup pstObj = new PstAppGroup(oid);
            pstObj.delete();
            return oid;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static int getCount(String whereClause){
        DBResultSet dbrs=null;
        try{
            int count = 0;
            String sql = " SELECT COUNT("+fieldNames[FLD_GROUP_ID] +") AS NRCOUNT" +
                         " FROM " + TBL_APP_GROUP;


            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            System.out.println(sql);
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()) {
                count = rs.getInt(1);
            }

            return count;
        }
        catch (Exception exc){
            System.out.println("getCount "+ exc);
            return 0;
        }
        finally{
            DBResultSet.close(dbrs);
        }

    }

    public static Vector list(int start , int recordToGet, String whereClause, String order)
    {
        Vector lists = new Vector();
        DBResultSet dbrs=null;
        try {
            String sql = "SELECT "+fieldNames[FLD_GROUP_ID] +
                         ", " + fieldNames[FLD_GROUP_NAME] +
                         ", " + fieldNames[FLD_REG_DATE] +
                         ", " + fieldNames[FLD_DESCRIPTION] +
                         " FROM " + TBL_APP_GROUP;


            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;


            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
						if(start == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " + start + ","+ recordToGet ;

                        break;

                 case DBHandler.DBSVR_POSTGRESQL :
						if(start == 0 && recordToGet == 0)
							sql = sql + "";
						else
							sql = sql + " LIMIT " +recordToGet + " OFFSET "+ start ;

                        break;

                 case DBHandler.DBSVR_SYBASE :
                    	break;

                 case DBHandler.DBSVR_ORACLE :
                    	break;

                 case DBHandler.DBSVR_MSSQL :
                    	break;

                default:
                    ;
            }

            System.out.println(sql);
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                AppGroup appPriv = new AppGroup();
                resultToObject(rs, appPriv);
                lists.add(appPriv);
            }
            return lists;

       }catch(Exception e) {
            System.out.println(e);
       }
       finally{
            DBResultSet.close(dbrs);
       }
       return new Vector();
    }


    private static void resultToObject(ResultSet rs, AppGroup appPriv) {
        try {
            appPriv.setOID(rs.getLong(fieldNames[FLD_GROUP_ID]));
            appPriv.setGroupName(rs.getString(fieldNames[FLD_GROUP_NAME]));
            appPriv.setRegDate(rs.getDate(fieldNames[FLD_REG_DATE]));
            appPriv.setDescription(rs.getString(fieldNames[FLD_DESCRIPTION]));
        }catch(Exception e){
            System.out.println("resultToObject() " + e.toString());
        }
    }

   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         AppGroup appGroup = PstAppGroup.fetch(oid);
         object.put(PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_ID], appGroup.getOID());
         object.put(PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME], appGroup.getGroupName ());
         object.put(PstAppGroup.fieldNames[PstAppGroup.FLD_REG_DATE], appGroup.getRegDate ());
         object.put(PstAppGroup.fieldNames[PstAppGroup.FLD_DESCRIPTION], appGroup.getDescription ());
      }catch(Exception exc){}
      return object;
   }

}
