/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.entity.email;

/**
 *
 * @author dimata005
 */
import java.sql.*;
import java.util.*;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;

public class PstSettingEmail extends DBHandler implements I_DBInterface, I_DBType, I_Persintent {

    public static final String TBL_APP_SETTING_EMAIL = "log_setting_email";
    public static final int FLD_EMAIL_ID		= 0;
    public static final int FLD_EMAIL_NAME		= 1;
    public static final int FLD_PASSWORD		= 2;
    public static final int FLD_HOST                    = 3;
    public static final int FLD_PORT			= 4;

    public static  final String[] fieldNames = {
        "EMAIL_ID", "EMAIL_NAME", "PASSWORD", "HOST", "PORT"
    } ;

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID, TYPE_STRING,   TYPE_STRING,  TYPE_STRING,  TYPE_STRING,  TYPE_STRING
    };
    
    public PstSettingEmail() {
    }

     public PstSettingEmail(int i) throws DBException {
        super(new PstSettingEmail());
    }


    public PstSettingEmail(String sOid) throws DBException {
        super(new PstSettingEmail(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }


    public PstSettingEmail(long lOid) throws DBException {
        super(new PstSettingEmail(0));
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
        return TBL_APP_SETTING_EMAIL;
    }

    public String getPersistentName() {
        return new PstSettingEmail().getClass().getName();
    }


    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }


    public long delete(Entity ent) {
        return delete((SettingEmail) ent);
    }

    public long insert(Entity ent){
        try{
            return PstSettingEmail.insert((SettingEmail) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }


    public long update(Entity ent) {
        return update((SettingEmail) ent);
    }

    public long fetch(Entity ent) {
        SettingEmail entObj = PstSettingEmail.fetch(ent.getOID());
        ent = (Entity)entObj;
        return ent.getOID();
    }

    public static SettingEmail fetch(long oid) {
        SettingEmail entObj = new SettingEmail();
        try {
            PstSettingEmail pstObj = new PstSettingEmail(oid);
            entObj.setOID(oid);
            entObj.setEmailName(pstObj.getString(FLD_EMAIL_NAME));
            entObj.setPassword(pstObj.getString(FLD_PASSWORD));
            entObj.setHost(pstObj.getString(FLD_HOST));
            entObj.setPort(pstObj.getString(FLD_PORT));
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }

     public static long insert(SettingEmail entObj) throws DBException  {
      PstSettingEmail pstObj = new PstSettingEmail(0);

        pstObj.setString(FLD_EMAIL_NAME, entObj.getEmailName());
        pstObj.setString(FLD_PASSWORD, entObj.getPassword());
        pstObj.setString(FLD_HOST, entObj.getHost());
        pstObj.setString(FLD_PORT, entObj.getPort());
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_EMAIL_ID));
        return entObj.getOID();
    }

    public static long update(SettingEmail entObj) {
        if( entObj!=null &&
            entObj.getOID()!=0 &&
            entObj.getEmailName()!=null && entObj.getEmailName().length()>0 &&
            entObj.getPassword()!=null && entObj.getPassword().length()>0
          )
        {
            try {
               PstSettingEmail pstObj = new PstSettingEmail(entObj.getOID());

                pstObj.setString(FLD_EMAIL_NAME, entObj.getEmailName());
                pstObj.setString(FLD_PASSWORD, entObj.getPassword());
                pstObj.setString(FLD_HOST, entObj.getHost());
                pstObj.setString(FLD_PORT, entObj.getPort());

                pstObj.update();
                return entObj.getOID();
            }catch(Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }

     private static void resultToObject(ResultSet rs, SettingEmail entObj) {
        try {
            entObj.setOID(rs.getLong(fieldNames[FLD_EMAIL_ID]));
            entObj.setEmailName(rs.getString(fieldNames[FLD_EMAIL_NAME]));
            entObj.setPassword(rs.getString(fieldNames[FLD_PASSWORD]));
            entObj.setHost(rs.getString(fieldNames[FLD_HOST]));
            entObj.setPort(rs.getString(fieldNames[FLD_PORT]));

        }catch(Exception e){
            System.out.println("resultToObject() appuser -> : " + e.toString());
        }
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_APP_SETTING_EMAIL + " ";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                SettingEmail settingEmail = new SettingEmail();
                resultToObject(rs, settingEmail);
                lists.add(settingEmail);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    public static int getCount(String whereClause){
        DBResultSet dbrs=null;
        try{
            int count = 0;
            String sql = " SELECT COUNT("+fieldNames[FLD_EMAIL_ID] +") AS NRCOUNT" +
            " FROM " + TBL_APP_SETTING_EMAIL;
            
            
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            //System.out.println(sql);
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
    
    
       public static long delete(long oid) {
        try {
            PstSettingEmail pstObj = new PstSettingEmail(oid);
            pstObj.delete();
            return oid;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }

}
