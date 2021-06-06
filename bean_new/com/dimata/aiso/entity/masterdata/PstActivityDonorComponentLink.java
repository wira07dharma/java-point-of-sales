/*
 * PstActivityDonorComponentLink.java
 *
 * Created on January 23, 2007, 1:54 PM
 */

package com.dimata.aiso.entity.masterdata;

/* import package aiso */
import com.dimata.aiso.db.*;

/* import package qdep */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

/* import package java sql */
import java.sql.ResultSet;

/* import package java util */
import java.util.*;

/**
 *
 * @author  dwi
 */
public class PstActivityDonorComponentLink extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_AISO_ACTIVITY_ASSIGN = "aiso_activity_assign";
    
    public static final int FLD_ACTIVITY_ASSIGN_ID = 0;
    public static final int FLD_ACTIVITY_ID = 1;
    public static final int FLD_ACTIVITY_PERIOD_ID = 2;
    public static final int FLD_DONOR_COMPONENT_ID = 3;
    public static final int FLD_SHARE_BUDGET = 4;
    public static final int FLD_SHARE_PROCENTAGE = 5;
    
    public static String[] fieldNames = {
        "ACTIVITY_ASSIGN_ID",
        "ACTIVITY_ID",
        "ACTIVITY_PERIOD_ID",
        "DONOR_COMPONENT_ID",
        "SHARE_BUDGET",
        "SHARE_PROCENTAGE"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    /** Creates a new instance of PstActivityDonorComponentLink */
    public PstActivityDonorComponentLink() {
    }
    
    public PstActivityDonorComponentLink(int i) throws DBException{
        super(new PstActivityDonorComponentLink());
    }
    
    public PstActivityDonorComponentLink(String sOid)throws DBException{
        super(new PstActivityDonorComponentLink(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstActivityDonorComponentLink(long Oid) throws DBException{
        super(new PstActivityDonorComponentLink(0));
        String sOid = "";
        try{
            sOid = String.valueOf(Oid);
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
        ActivityDonorComponentLink objActivityDonorComponentLink = PstActivityDonorComponentLink.fetchExc(ent.getOID());
        ent = (Entity)objActivityDonorComponentLink;
        return objActivityDonorComponentLink.getOID();
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
        return new PstActivityDonorComponentLink().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_ACTIVITY_ASSIGN;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((ActivityDonorComponentLink) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((ActivityDonorComponentLink) ent);
    }
    
    public static ActivityDonorComponentLink fetchExc(long Oid) throws DBException{
        try{
            ActivityDonorComponentLink objActivityDonorComponentLink = new ActivityDonorComponentLink();
            PstActivityDonorComponentLink pstActivityDonorComponentLink = new PstActivityDonorComponentLink(Oid);
            
            objActivityDonorComponentLink.setOID(Oid);
            objActivityDonorComponentLink.setActivityId(pstActivityDonorComponentLink.getlong(FLD_ACTIVITY_ID));
            objActivityDonorComponentLink.setActivityPeriodId(pstActivityDonorComponentLink.getlong(FLD_ACTIVITY_PERIOD_ID));
            objActivityDonorComponentLink.setDonorComponentId(pstActivityDonorComponentLink.getlong(FLD_DONOR_COMPONENT_ID));
            objActivityDonorComponentLink.setShareBudget(pstActivityDonorComponentLink.getfloat(FLD_SHARE_BUDGET));
            objActivityDonorComponentLink.setShareProcentage(pstActivityDonorComponentLink.getfloat(FLD_SHARE_PROCENTAGE));
            
            return objActivityDonorComponentLink;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityDonorComponentLink(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(ActivityDonorComponentLink objActivityDonorComponentLink) throws DBException{
        try{
            PstActivityDonorComponentLink pstActivityDonorComponentLink = new PstActivityDonorComponentLink(0);
            
            pstActivityDonorComponentLink.setLong(FLD_ACTIVITY_ID, objActivityDonorComponentLink.getActivityId());
            pstActivityDonorComponentLink.setLong(FLD_ACTIVITY_PERIOD_ID, objActivityDonorComponentLink.getActivityPeriodId());
            pstActivityDonorComponentLink.setLong(FLD_DONOR_COMPONENT_ID, objActivityDonorComponentLink.getDonorComponentId());
            pstActivityDonorComponentLink.setFloat(FLD_SHARE_BUDGET, objActivityDonorComponentLink.getShareBudget());
            pstActivityDonorComponentLink.setFloat(FLD_SHARE_PROCENTAGE, objActivityDonorComponentLink.getShareProcentage());
            
            pstActivityDonorComponentLink.insert();            
            objActivityDonorComponentLink.setOID(pstActivityDonorComponentLink.getlong(FLD_ACTIVITY_ASSIGN_ID));
            
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityDonorComponentLink(0), DBException.UNKNOWN);
        }
        return objActivityDonorComponentLink.getOID();
    }
    
    public static long updateExc(ActivityDonorComponentLink objActivityDonorComponentLink) throws DBException{
        try{
            if(objActivityDonorComponentLink != null && objActivityDonorComponentLink.getOID() != 0){
                PstActivityDonorComponentLink pstActivityDonorComponentLink = new PstActivityDonorComponentLink(objActivityDonorComponentLink.getOID());
                
                pstActivityDonorComponentLink.setLong(FLD_ACTIVITY_ID, objActivityDonorComponentLink.getActivityId());
                pstActivityDonorComponentLink.setLong(FLD_ACTIVITY_PERIOD_ID, objActivityDonorComponentLink.getActivityPeriodId());
                pstActivityDonorComponentLink.setLong(FLD_DONOR_COMPONENT_ID, objActivityDonorComponentLink.getDonorComponentId());
                pstActivityDonorComponentLink.setFloat(FLD_SHARE_BUDGET, objActivityDonorComponentLink.getShareBudget());
                pstActivityDonorComponentLink.setFloat(FLD_SHARE_PROCENTAGE, objActivityDonorComponentLink.getShareProcentage());
                
                pstActivityDonorComponentLink.update();
                return  objActivityDonorComponentLink.getOID();           
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityDonorComponentLink(0), DBException.UNKNOWN);
        }
        return 0;    
    }
    
    public static long deleteExc(long Oid) throws DBException{
        try{
            PstActivityDonorComponentLink pstActivityDonorComponentLink = new PstActivityDonorComponentLink(Oid);
            pstActivityDonorComponentLink.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstActivityDonorComponentLink(0), DBException.UNKNOWN);
        }
        return Oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_ACTIVITY_ASSIGN + " ";
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
            
            System.out.println("SQL PstActivityActDonorCompLink.list() ::: "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                ActivityDonorComponentLink objActivityDonorComponentLink = new ActivityDonorComponentLink();
                resultToObject(rs, objActivityDonorComponentLink);
                lists.add(objActivityDonorComponentLink);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new ActivityAccountLink().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
       
       private static void resultToObject(ResultSet rs, ActivityDonorComponentLink objActivityDonorComponentLink) {
        try {

            objActivityDonorComponentLink.setOID(rs.getLong(PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_ACTIVITY_ASSIGN_ID]));
            objActivityDonorComponentLink.setActivityId(rs.getLong(PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_ACTIVITY_ID]));            
            objActivityDonorComponentLink.setActivityPeriodId(rs.getLong(PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_ACTIVITY_PERIOD_ID]));            
            objActivityDonorComponentLink.setDonorComponentId(rs.getLong(PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_DONOR_COMPONENT_ID]));
            objActivityDonorComponentLink.setShareBudget(rs.getFloat(PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_SHARE_BUDGET]));
            objActivityDonorComponentLink.setShareProcentage(rs.getFloat(PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_SHARE_PROCENTAGE]));
            
        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }
       
       public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstActivityDonorComponentLink.fieldNames[PstActivityDonorComponentLink.FLD_ACTIVITY_ASSIGN_ID] + ") " +
                    " FROM " + TBL_AISO_ACTIVITY_ASSIGN;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
                
            System.out.println("SQL PstActivityActDonorCompLink.getCount() ::: "+sql);
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
       
   public static boolean checkDonorComp(long idDonorCompId, long lActPeriodeId, long lActivityId){
       DBResultSet dbrs = null;
       try{
            String whClause = fieldNames[PstActivityDonorComponentLink.FLD_DONOR_COMPONENT_ID]+" = "+idDonorCompId+
                              " AND "+fieldNames[PstActivityDonorComponentLink.FLD_ACTIVITY_PERIOD_ID]+" = "+lActPeriodeId+
                              " AND "+fieldNames[PstActivityDonorComponentLink.FLD_ACTIVITY_ID]+" = "+lActivityId;  
            Vector vList = (Vector)list(0,0,whClause,"");
            
            ActivityDonorComponentLink objActDncLink = new ActivityDonorComponentLink();
            if(vList != null && vList.size() > 0){
                for(int i = 0; i < vList.size(); i++){
                    objActDncLink = (ActivityDonorComponentLink)vList.get(i);
                }
            }
            
            if(objActDncLink.getDonorComponentId() != 0)
                return true;
        }catch(Exception e){
            System.out.println("Exception on PstActivityDonorCompLink :::::: "+e.toString());
        }
        return false;
   }
}
