/*
 * PstActivityAccountLink.java
 *
 * Created on January 13, 2007, 9:18 AM
 */

package com.dimata.aiso.entity.masterdata;

/* import package aiso */
import com.dimata.aiso.db.*;

/* import package qdep */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

/* import package java util */
import java.util.*;

/* import package java sql */
import java.sql.ResultSet;



/**
 *
 * @author  dwi
 */
public class PstActivityAccountLink extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_AISO_ACTIVITY_ACCOUNT = "aiso_activity_account";
    
    public static final int FLD_ACT_ACC_LINK_ID = 0;
    public static final int FLD_ACCOUNT_ID = 1;
    public static final int FLD_ACTIVITY_ID = 2;
    
    public static String[] fieldNames = {
        "ACT_ACC_LINK_ID",
        "ID_PERKIRAAN",
        "ACTIVITY_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG        
    };
    
    /** Creates a new instance of PstActivityAccountLink */
    public PstActivityAccountLink() {
    }
    
    public PstActivityAccountLink(int i) throws DBException { 
        super(new PstActivityAccountLink()); 
    }
    
    public PstActivityAccountLink(String sOid) throws DBException {
        super(new PstActivityAccountLink(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstActivityAccountLink(long lOid) throws DBException {
        super(new PstActivityAccountLink(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e){
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public long deleteExc(Entity ent) throws Exception {
        if(ent == null){
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }         
        return deleteExc(ent.getOID());        
    }
    
    public long fetchExc(Entity ent) throws Exception {
        ActivityAccountLink objActivityAccountLink = PstActivityAccountLink.fetchExc(ent.getOID()); 
        ent = (Entity)objActivityAccountLink;
        return objActivityAccountLink.getOID();
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstActivityAccountLink().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_ACTIVITY_ACCOUNT;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((ActivityAccountLink) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((ActivityAccountLink) ent);
    }
    
    public static ActivityAccountLink fetchExc(long Oid) throws DBException {
        try{
            ActivityAccountLink objActivityAccountLink = new ActivityAccountLink();
            PstActivityAccountLink pstPstActivityAccountLink = new PstActivityAccountLink(Oid);
            
            objActivityAccountLink.setOID(Oid);
            objActivityAccountLink.setIdPerkiraan(pstPstActivityAccountLink.getlong(FLD_ACCOUNT_ID));
            objActivityAccountLink.setActivityId(pstPstActivityAccountLink.getlong(FLD_ACTIVITY_ID));
            
            return objActivityAccountLink;
            
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityAccountLink(0), DBException.UNKNOWN);
        }    
    }
    
    public static long insertExc(ActivityAccountLink objActivityAccountLink) throws DBException {
        try{
            PstActivityAccountLink pstActivityAccountLink = new PstActivityAccountLink(0);
            
            pstActivityAccountLink.setLong(FLD_ACCOUNT_ID, objActivityAccountLink.getIdPerkiraan());
            pstActivityAccountLink.setLong(FLD_ACTIVITY_ID, objActivityAccountLink.getActivityId());
            
            pstActivityAccountLink.insert();
            objActivityAccountLink.setOID(pstActivityAccountLink.getlong(FLD_ACT_ACC_LINK_ID));
            
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityAccountLink(0), DBException.UNKNOWN);
        }
        return objActivityAccountLink.getOID();
    }
    
    public static long updateExc(ActivityAccountLink objActivityAccountLink) throws DBException{
        try{
            if(objActivityAccountLink != null && objActivityAccountLink.getOID() != 0){
                PstActivityAccountLink pstActivityAccountLink = new PstActivityAccountLink(objActivityAccountLink.getOID());
                
                pstActivityAccountLink.setLong(FLD_ACCOUNT_ID, objActivityAccountLink.getIdPerkiraan());
                pstActivityAccountLink.setLong(FLD_ACTIVITY_ID, objActivityAccountLink.getActivityId());
                
                pstActivityAccountLink.update();
                return objActivityAccountLink.getOID();
            }        
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityAccountLink(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long Oid) throws DBException {
        try{
            PstActivityAccountLink pstActivityAccountLink = new PstActivityAccountLink(Oid);
            pstActivityAccountLink.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityAccountLink(0), DBException.UNKNOWN);
        }
        return Oid;
    }
    
     public static Vector list(int limitStart, int recordToGet, String whereClause, String order) { 
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_ACTIVITY_ACCOUNT + " ";
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("Sql pd PstActivityAccountLink ===> "+sql);

            while (rs.next()) {
                ActivityAccountLink objActivityAccountLink = new ActivityAccountLink();
                resultToObject(rs, objActivityAccountLink);
                lists.add(objActivityAccountLink);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new ActivityAccountLink().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
       
       private static void resultToObject(ResultSet rs, ActivityAccountLink objActivityAccountLink) {
        try {

            objActivityAccountLink.setOID(rs.getLong(PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACT_ACC_LINK_ID]));
            objActivityAccountLink.setIdPerkiraan(rs.getLong(PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]));            
            objActivityAccountLink.setActivityId(rs.getLong(PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]));            

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }
       
       public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACT_ACC_LINK_ID] + ") " +
                    " FROM " + TBL_AISO_ACTIVITY_ACCOUNT;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
       
       public static boolean checkCoAId(long lCoAId, long lActId){
           DBResultSet dbrs = null;          
           try{
                String sql = " SELECT "+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+
                            " FROM "+PstActivityAccountLink.TBL_AISO_ACTIVITY_ACCOUNT+
                            " WHERE "+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+
                            " = "+lCoAId+" AND "+
                            PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]+" = "+lActId;
                
                //System.out.println("SQL PstActivityAccLink.checkCoAId :::: "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    return true;
                }
           }catch(Exception e){
                
                System.out.println("Exception on PstActitivityAccountLink.checkCoAId() ::::: "+e.toString());
                e.printStackTrace();
           }
           return false;
       }
       
}
