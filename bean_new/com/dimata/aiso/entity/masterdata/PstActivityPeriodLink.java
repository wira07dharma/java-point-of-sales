/*
 * PstActivityPeriodLink.java
 *
 * Created on January 16, 2007, 11:03 AM
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
public class PstActivityPeriodLink extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_AISO_ACT_PERIOD_LINK = "aiso_act_link_period";
    
    public static final int FLD_ACT_LINK_PERIOD_ID = 0;
    public static final int FLD_ACT_PERIOD_ID = 1;
    public static final int FLD_ACTIVITY_ID = 2;
    public static final int FLD_BUDGET = 3;
    
    public static String[] fieldNames = {
        "ACT_LINK_PERIOD_ID",
        "ACTIVITY_PERIOD_ID",
        "ACTIVITY_ID",
        "BUDGET"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FLOAT
    };    
    
    /** Creates a new instance of PstActivityPeriodLink */
    public PstActivityPeriodLink() {
    }
    
    public PstActivityPeriodLink(int i) throws DBException {
        super(new PstActivityPeriodLink());    
    } 
    
    public PstActivityPeriodLink(String sOid) throws DBException {
        super(new PstActivityPeriodLink(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstActivityPeriodLink(long lOid) throws DBException {
        super(new PstActivityPeriodLink(0));
        String sOid = "0";
        try{
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
        ActivityPeriodLink objActivityPeriodLink = PstActivityPeriodLink.fetchExc(ent.getOID()); 
        ent =(Entity)objActivityPeriodLink;
        return objActivityPeriodLink.getOID();
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
        return new PstActivityPeriodLink().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_ACT_PERIOD_LINK;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((ActivityPeriodLink)ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((ActivityPeriodLink)ent);
    }
    
    public static ActivityPeriodLink fetchExc(long Oid) throws DBException {
        try{
            ActivityPeriodLink objActivityPeriodLink = new ActivityPeriodLink();
            PstActivityPeriodLink pstActivityPeriodLink = new PstActivityPeriodLink(Oid);
            
            objActivityPeriodLink.setOID(Oid);
            objActivityPeriodLink.setActivityPeriodId(pstActivityPeriodLink.getlong(FLD_ACT_PERIOD_ID));
            objActivityPeriodLink.setActivityId(pstActivityPeriodLink.getlong(FLD_ACTIVITY_ID));
            objActivityPeriodLink.setBudget(pstActivityPeriodLink.getfloat(FLD_BUDGET));
            
            return objActivityPeriodLink;
        
        }catch(DBException dbe){
            throw dbe; 
        }catch(Exception e){
            throw new DBException(new PstActivityPeriodLink(0), DBException.UNKNOWN); 
        }
    }
    
    public static long insertExc(ActivityPeriodLink objActivityPeriodLink) throws DBException {
        try{
            PstActivityPeriodLink pstActivityPeriodLink = new PstActivityPeriodLink(0);
            
            pstActivityPeriodLink.setLong(FLD_ACT_PERIOD_ID, objActivityPeriodLink.getActivityPeriodId());
            pstActivityPeriodLink.setLong(FLD_ACTIVITY_ID, objActivityPeriodLink.getActivityId());
            pstActivityPeriodLink.setFloat(FLD_BUDGET, objActivityPeriodLink.getBudget());
            
            pstActivityPeriodLink.insert();
            objActivityPeriodLink.setOID(pstActivityPeriodLink.getlong(FLD_ACT_LINK_PERIOD_ID));
            
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityPeriodLink(0), DBException.UNKNOWN);
        }
        return objActivityPeriodLink.getOID();
    }
    
    public static long updateExc(ActivityPeriodLink objActivityPeriodLink) throws DBException {
    
        try{
            if(objActivityPeriodLink != null && objActivityPeriodLink.getOID() > 0){
                
                PstActivityPeriodLink pstActivityPeriodLink = new PstActivityPeriodLink(objActivityPeriodLink.getOID());
                
                pstActivityPeriodLink.setLong(FLD_ACT_PERIOD_ID, objActivityPeriodLink.getActivityPeriodId());
                pstActivityPeriodLink.setLong(FLD_ACTIVITY_ID, objActivityPeriodLink.getActivityId());
                pstActivityPeriodLink.setFloat(FLD_BUDGET, objActivityPeriodLink.getBudget());
            
                pstActivityPeriodLink.update();
                return objActivityPeriodLink.getOID();
            
            }        
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityPeriodLink(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long Oid) throws DBException{
        try{
            PstActivityPeriodLink pstActivityPeriodLink = new PstActivityPeriodLink(Oid);
            pstActivityPeriodLink.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityPeriodLink(0), DBException.UNKNOWN);
        }
        return Oid;
    }
    
     public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_ACT_PERIOD_LINK + " ";
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
            System.out.println("Sql PstActivityPeriodLink ===> "+sql);
            while (rs.next()) {
                ActivityPeriodLink objActivityPeriodLink = new ActivityPeriodLink();
                resultToObject(rs, objActivityPeriodLink);
                lists.add(objActivityPeriodLink);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new ActivityPeriodLink().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
       
       public static void resultToObject(ResultSet rs, ActivityPeriodLink objActivityPeriodLink) {
        try {

            objActivityPeriodLink.setOID(rs.getLong(PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACT_LINK_PERIOD_ID]));
            objActivityPeriodLink.setActivityPeriodId(rs.getLong(PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACT_PERIOD_ID]));            
            objActivityPeriodLink.setActivityId(rs.getLong(PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACTIVITY_ID]));
            objActivityPeriodLink.setBudget(rs.getFloat(PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_BUDGET]));

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }
       
       public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACT_LINK_PERIOD_ID] + ") " +
                    " FROM " + TBL_AISO_ACT_PERIOD_LINK;
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
       
       public static ActivityPeriodLink checkOID(long oidPeriod, long activityOid){
        DBResultSet dbrs = null;
        ActivityPeriodLink activityPeriodLink = new ActivityPeriodLink();
        try{
            String sql = "SELECT * FROM " + TBL_AISO_ACT_PERIOD_LINK + " WHERE " +
            PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACT_PERIOD_ID] + " = " + oidPeriod+
            " and "+PstActivityPeriodLink.fieldNames[PstActivityPeriodLink.FLD_ACTIVITY_ID ] + " = " + activityOid;
            
            System.out.println("SQL PstActivityPeriodLink.checkOID() :::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) { 
                resultToObject(rs,activityPeriodLink);
            }
            rs.close();
        }catch(Exception e){
            activityPeriodLink = new ActivityPeriodLink();    
            System.out.println("err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            return activityPeriodLink;
        }
    }       
     
       // cek for delete activity period link
     public static void checkForDelete(Vector listActivityOld, Vector listActivityNew){
        DBResultSet dbrs = null;
        try{
            if(listActivityOld!=null && listActivityOld.size()>0){
                for(int i=0;i<listActivityOld.size();i++){
                    ActivityPeriodLink objActivityPeriodLink = (ActivityPeriodLink)listActivityOld.get(i);
                    if(listActivityNew!=null && listActivityNew.size()>0){
                        boolean deleteStatus = true;
                        
                        // bandingkan data database dengan vector request
                        // jika di database ada dan di request tidak ada maka status delete = true, dan sebaliknya                         
                        for(int j=0;j<listActivityNew.size();j++){
                            Vector vect = (Vector) listActivityNew.get(j);
                            long oidActivityPeriod = Long.parseLong((String) vect.get(0));
                            long oidActivity = Long.parseLong((String) vect.get(1));
                            if(objActivityPeriodLink.getActivityId()==oidActivity){
                                if(objActivityPeriodLink.getActivityPeriodId()==oidActivityPeriod){
                                    deleteStatus = false;
                                    break;
                                }
                            }
                        }
                        if(deleteStatus){
                            System.out.println("delete data ak period : "+objActivityPeriodLink.getOID());
                            deleteExc(objActivityPeriodLink.getOID());
                        }
                    }
                }
            }
        }catch(Exception e){
            System.out.println("err : "+e.toString());
        }
    }              

       public static long deleteByOid(long Oid) {
        try {
            String sql = "DELETE FROM " + TBL_AISO_ACT_PERIOD_LINK+
            " WHERE "+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACT_ACC_LINK_ID] +
            " = " + Oid ;
            
            int delete = DBHandler.execUpdate(sql);
            return Oid;
        }catch(Exception exc) {
            System.out.println(" exception delete by Owner ID "+exc.toString());
        }
        return 0;
    }
    

}
