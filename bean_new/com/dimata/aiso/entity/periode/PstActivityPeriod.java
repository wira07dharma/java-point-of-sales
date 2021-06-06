/*
 * PstActivityPeriod.java
 *
 * Created on January 4, 2007, 1:31 PM
 */

package com.dimata.aiso.entity.periode;

/**
 *
 * @author  dwi
 */

/**
    import package java
 */
import java.util.*;
import java.io.*;
import java.sql.*;

/**
    import package qdep
 */

import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Formater;

public class PstActivityPeriod extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_ACTIVITY_PERIOD = "aiso_activity_period";
    public static final int FLD_ACTIVITY_PERIOD_ID = 0;
    public static final int FLD_NAME = 1;
    public static final int FLD_START_DATE = 2;
    public static final int FLD_END_DATE = 3;
    public static final int FLD_DESCRIPTION = 4;
    public static final int FLD_POSTED = 5;
    
    public static final int PERIOD_OPEN = 0;
    public static final int PERIOD_CLOSED = 1;
    public static final int PERIOD_PREPARE_OPEN = 2;
    
    public static String [] fieldNames = {
        "ACTIVITY_PERIOD_ID",
        "NAME",
        "START_DATE",
        "END_DATE",
        "DESCRIPTION",
        "POSTED"
    };
    
    public static int [] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT
    };
    

    /** Creates a new instance of PstActivityPeriod */
    public PstActivityPeriod() {
    }
    
    public PstActivityPeriod(int i) throws DBException {
        super(new PstActivityPeriod());
    }
    
    public PstActivityPeriod(String sOid) throws DBException {
        super(new PstActivityPeriod(0));
        if(!locate(sOid))
           throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;            
    }
    
    public PstActivityPeriod(long lOid) throws DBException {
        super(new PstActivityPeriod(0));
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
        return new PstActivityPeriod().getClass().getName();
    } 
    
    public String getTableName() {
        return TBL_ACTIVITY_PERIOD;
    }
    
    
    
    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND) ;
        }
         return deleteExc(ent.getOID());
    }
    
    public long fetchExc(Entity ent) throws Exception{
        ActivityPeriod actPeriod = PstActivityPeriod.fetchExc(ent.getOID());
        ent = (Entity)actPeriod;
        return actPeriod.getOID();
    }   
    
    public long insertExc(Entity ent) throws Exception {
         return PstActivityPeriod.insertExc((ActivityPeriod) ent);        
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((ActivityPeriod) ent);
    }
    
    public static ActivityPeriod fetchExc(long oid) throws DBException {
        try {
            ActivityPeriod actPeriode = new ActivityPeriod();
            PstActivityPeriod pActPeriode = new PstActivityPeriod(oid);
            actPeriode.setOID(oid);
            actPeriode.setStartDate(pActPeriode.getDate(FLD_START_DATE));
            actPeriode.setEndDate(pActPeriode.getDate(FLD_END_DATE));
            actPeriode.setName(pActPeriode.getString(FLD_NAME));
            actPeriode.setDescription(pActPeriode.getString(FLD_DESCRIPTION));
            actPeriode.setPosted(pActPeriode.getInt(FLD_POSTED));            
            return actPeriode;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException( new PstActivityPeriod(0), DBException.UNKNOWN);
        }
    }
    
     public static long insertExc(ActivityPeriod actPeriode) throws DBException {
        try {
            PstActivityPeriod pActPeriode = new PstActivityPeriod(0);
            pActPeriode.setDate(FLD_START_DATE, actPeriode.getStartDate());
            pActPeriode.setDate(FLD_END_DATE, actPeriode.getEndDate());
            pActPeriode.setString(FLD_NAME, actPeriode.getName());
            pActPeriode.setString(FLD_DESCRIPTION, actPeriode.getDescription());
            pActPeriode.setInt(FLD_POSTED, actPeriode.getPosted());            
            pActPeriode.insert();
            actPeriode.setOID(pActPeriode.getlong(FLD_ACTIVITY_PERIOD_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstActivityPeriod(0), DBException.UNKNOWN);
        }
        return actPeriode.getOID();
    }
     
      public static long updateExc(ActivityPeriod actPeriode) throws DBException {
        try {
            if (actPeriode.getOID() != 0) {
                PstActivityPeriod pActPeriode = new PstActivityPeriod(actPeriode.getOID());
                pActPeriode.setDate(FLD_START_DATE, actPeriode.getStartDate());
                pActPeriode.setDate(FLD_END_DATE, actPeriode.getEndDate());
                pActPeriode.setString(FLD_NAME, actPeriode.getName());
                pActPeriode.setString(FLD_DESCRIPTION, actPeriode.getDescription());
                pActPeriode.setInt(FLD_POSTED, actPeriode.getPosted());                
                pActPeriode.update();
                return actPeriode.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstActivityPeriod(0), DBException.UNKNOWN);
        }
        return 0;
    }
      
      public static long deleteExc(long oid) throws DBException {
        try {
            PstActivityPeriod pActPeriode = new PstActivityPeriod(oid);
            pActPeriode.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstActivityPeriod(0), DBException.UNKNOWN);
        }
        return oid;
    }
      
      public static Vector listAll() {
        return list(0, 500, "","");
    }
      
       public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT * FROM " + TBL_ACTIVITY_PERIOD + " ";
            
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL :
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;
                    
                    break;
                    
                case DBHandler.DBSVR_SYBASE :
                    break;
                    
                case DBHandler.DBSVR_ORACLE :
                    break;
                    
                case DBHandler.DBSVR_MSSQL :
                    break;
                    
                default:
                    break;
            }
            
            System.out.println("sql : " + sql); 
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                ActivityPeriod actPeriode = new ActivityPeriod();
                resultToObject(rs, actPeriode);
                lists.add(actPeriode);
            }
            rs.close();
        } catch(Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
       
       private static void resultToObject(ResultSet rs, ActivityPeriod actPeriode) {
        try {
            actPeriode.setOID(rs.getLong(fieldNames[FLD_ACTIVITY_PERIOD_ID]));
            actPeriode.setStartDate(rs.getDate(fieldNames[FLD_START_DATE]));
            actPeriode.setEndDate(rs.getDate(fieldNames[FLD_END_DATE]));
            actPeriode.setName(rs.getString(fieldNames[FLD_NAME]));
            actPeriode.setDescription(rs.getString(fieldNames[FLD_DESCRIPTION]));
            actPeriode.setPosted(rs.getInt(fieldNames[FLD_POSTED]));            
        } catch (Exception e) {
        }
    }
       
        public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT COUNT(" + fieldNames[FLD_ACTIVITY_PERIOD_ID]
            + ") FROM " + TBL_ACTIVITY_PERIOD;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
        
    public static long setPeriodPosted(long lCurrPeriodOid) 
    {
        try  
        {
            String sql = "UPDATE " + TBL_ACTIVITY_PERIOD
            + " SET " + fieldNames[FLD_POSTED]
            + " = " + PERIOD_CLOSED
            + " WHERE " + fieldNames[FLD_ACTIVITY_PERIOD_ID] 
            + " = " + lCurrPeriodOid;
            
            DBHandler.execUpdate(sql);
            return lCurrPeriodOid;
        }
        catch (Exception e) 
        {
            System.out.println("Err Update Posted : " + e.toString());
            return 0;
        }
    }
    
    public static boolean openPrepareOpenPeriod(long lastPeriodOid) 
    {
        boolean bReturn = false;
        DBResultSet dbrs = null;        
        try 
        {
            ActivityPeriod actPeriode = fetchExc(lastPeriodOid);
            
            if (actPeriode.getOID() != 0) 
            {                
                java.util.Date lastDate = actPeriode.getEndDate();
                int date = lastDate.getDate();
                int month = lastDate.getMonth();
                int year = lastDate.getYear();
                java.util.Date newDate = new java.util.Date(year, month, date+1);
                String strStartDate = Formater.formatDate(newDate, "yyyy-MM-dd");                
                
                String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] + 
                             ", " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_NAME] +
                             ", " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_DESCRIPTION] +
                             ", " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] +
                             ", " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE] +                            
                             ", " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_POSTED] +
                             " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
                             " WHERE " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] +
                             " = '" + strStartDate + "'" + 
                             " ORDER BY " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID];
                
                System.out.println("sql : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet(); 
                
                while(rs.next()) 
                {
                    long lPeriodOid = rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]);                    
                    if (lPeriodOid > 0) 
                    {
                        ActivityPeriod actPeriod = new ActivityPeriod();
                        actPeriod.setOID(lPeriodOid);
                        actPeriod.setName(rs.getString(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_NAME]));
                        actPeriod.setDescription(rs.getString(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_DESCRIPTION]));
                        actPeriod.setStartDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE]));
                        actPeriod.setEndDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]));                        
                        actPeriod.setPosted(PstActivityPeriod.PERIOD_OPEN);
                        
                        long updatedPeriodOid = PstActivityPeriod.updateExc(actPeriod);
                        if(updatedPeriodOid > 0)
                        {
                            return true;
                        }
                    }
                    else
                    {
                        return false;                        
                    }
                }                
                rs.close();                
            }
        }
        catch (Exception error)  
        {
            System.out.println(".:: PstActivityPeriode >> Method openPrepareOpenPeriod : " + error.toString());
        }
        finally 
        {
            DBResultSet.close(dbrs);
        }
        return bReturn;
    }
    
    public static java.util.Date getFirstDateOfNewPeriod() {
        java.util.Date resultDate = null;
        
        try {
            String sql = "SELECT " + fieldNames[FLD_END_DATE]
            + " FROM " + TBL_ACTIVITY_PERIOD
            + " ORDER BY " + fieldNames[FLD_END_DATE]
            + " DESC";
            DBResultSet dbrs = DBHandler.execQueryResult(sql);
            ResultSet rslSet = dbrs.getResultSet();
            if (rslSet.next()) {
                java.sql.Date lastDate = rslSet.getDate(fieldNames[FLD_END_DATE]);
                resultDate = new java.util.Date(lastDate.getYear(), lastDate.getMonth(), lastDate.getDate() + 1);
            }
            rslSet.close();
            DBResultSet.close(dbrs);
        } catch (Exception error) {
            System.out.println(".:: PstPeriode - getFirstDateOfNewPeriod : " + error.toString());
        }
        if (resultDate == null) {
            resultDate = new java.util.Date();
            int year = resultDate.getYear();
            int month = resultDate.getMonth();
            resultDate = new java.util.Date(year, month, 1);
        }
        return resultDate;
    }
    
     public static long getLastPeriodeOid() {
        long lastPeriodOid = 0;
        try {
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]
            + " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD
            + " ORDER BY " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]
            + " DESC";
            DBResultSet dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                lastPeriodOid = rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]);
            }
            rs.close();
            DBResultSet.close(dbrs);
        } catch (Exception error) {
            System.out.println(error.toString());
        }
        return lastPeriodOid;
    }
     
     public static Vector listPeriode(long periodeOID) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        try{
            if(periodeOID!=0){ 
                String sql = "SELECT "+PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]+", "+
                PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_NAME]+", "+
                PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_DESCRIPTION]+", "+
                PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE]+", "+
                PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]+", "+                
                " FROM "+PstActivityPeriod.TBL_ACTIVITY_PERIOD +
                " WHERE "+PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
                " = "+periodeOID;
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                
                while(rs.next()) {
                    ActivityPeriod actPeriod = new ActivityPeriod();
                    
                    actPeriod.setOID(rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]));
                    actPeriod.setName(rs.getString(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_NAME]));
                    actPeriod.setDescription(rs.getString(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_DESCRIPTION]));
                    actPeriod.setStartDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE]));
                    actPeriod.setEndDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]));                   
                    
                    result.add(actPeriod);
                    System.out.println("SQL METHOD VECTOR LIST PERIOD ::: "+sql);
                }
            }
        } catch (Exception e) {
            System.out.println("Err list period : "+e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
     
     public static long listPeriode(java.util.Date lastDate) {
        DBResultSet dbrs = null;
        long result = 0;
        String strDate = "\"" + Formater.formatDate(lastDate, "yyyy-MM-dd") + "\"";
        try{
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
            " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
            " WHERE " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE] +
            " < " + strDate +
            " ORDER BY " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE] +
            " DESC";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("SQL METHOD LONG LIST PERIODE :::: "+sql);
            while(rs.next()) {
                result = rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]);
                break;
            }
        } catch(Exception e) {
            System.out.println("Err list period : "+e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
     
      public static long getPeriodeIdBetween(java.util.Date selectedDate) {
        DBResultSet dbrs = null;
        long result = 0;
        String strDate = "'" + Formater.formatDate(selectedDate, "yyyy-MM-dd") + "'";
        try{
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
            " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
            " WHERE " + strDate + " BETWEEN " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] +
            " AND " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            System.out.println("SQL METHOD long getPeriodeIdBetween ::: "+sql);
            while(rs.next()) {
                result = rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]);
                break;
            }
        } catch(Exception e) {
            System.out.println("Err list period : "+e.toString());
        } finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
      
      public static ActivityPeriod getFirstPeriod(){
        ActivityPeriod actPeriod = new ActivityPeriod();
        String orderBy = PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE];
        Vector lists = PstActivityPeriod.list(0,0,"",orderBy);
        if(lists!=null && lists.size()>0){
            System.out.println("masuk ke first period");
            actPeriod = (ActivityPeriod)lists.get(0);
            System.out.println("oidnya : "+actPeriod.getOID());
        }
        return actPeriod;
    }
      
      public static long getFirstPeriodId(){
        long result = 0;
        String orderBy = PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE];
        Vector lists = PstActivityPeriod.list(0,0,"",orderBy);
        if(lists!=null && lists.size()>0){
            System.out.println("masuk ke first period");
            ActivityPeriod actPeriod = (ActivityPeriod)lists.get(0);
            result = actPeriod.getOID();
        }
        return result;
    }
      
      public static Vector getCurrPeriod() {
        DBResultSet dbrs = null;
        Vector lists = new Vector(1, 1);
        try {
            String sql = "SELECT * FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD
            + " WHERE " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_POSTED]
            + " = " + PstActivityPeriod.PERIOD_OPEN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                ActivityPeriod actPeriod = new ActivityPeriod();
                actPeriod.setOID(rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]));
                actPeriod.setStartDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE]));
                actPeriod.setEndDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]));
                actPeriod.setName(rs.getString(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_NAME]));
                actPeriod.setDescription(rs.getString(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_DESCRIPTION]));
                actPeriod.setPosted(rs.getInt(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_POSTED]));                
                lists.add(actPeriod);
            }
        } catch (Exception e) {
            System.out.println("Err CurrPeriod : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
      
       public static Vector getLastPeriod() {
        DBResultSet dbrs = null;
        Vector lists = new Vector(1,1);
        Vector listCurrPeriod = getCurrPeriod();
      
        if (listCurrPeriod.size() == 0) {
            try {
                String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]
                + ", " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_POSTED]
                + " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD
                + " ORDER BY " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]
                + " DESC";
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                
                while (rs.next()) {
                    ActivityPeriod actPeriod = new ActivityPeriod();
                    actPeriod.setEndDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]));
                    actPeriod.setPosted(rs.getInt(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_POSTED]));
                    lists.add(actPeriod);
                }
            } catch (Exception e) {
                System.out.println("Err LastPeriod : " + e.toString());
            } finally {
                DBResultSet.close(dbrs);
                System.out.println("lists : " + lists.size());
            }
            return lists;
        } else {
            return listCurrPeriod;
        }
    }
       
       public static boolean isTherePeriod(){
        Vector vectPeriod = getCurrPeriod();
        if(vectPeriod!=null && vectPeriod.size()>0){
            return true;
        }else{
            return false;
        }
    }
       
        public static long getCurrPeriodId() {
        Vector vectCurrPeriod = getCurrPeriod();
        long currPeriodId = 0;
        if (vectCurrPeriod != null && vectCurrPeriod.size() > 0) {
            ActivityPeriod actPeriod = (ActivityPeriod) vectCurrPeriod.get(0);
            return actPeriod.getOID();
        }
        return currPeriodId;
    }
        
         public static long getPerIdStartEntry(java.util.Date selectedDate) {
        DBResultSet dbrs = null;
        long result = 0;
        String strDate = "\"" + Formater.formatDate(selectedDate, "yyyy-MM-dd") + "\"";
        try{
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
            " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
            " WHERE " + strDate + " BETWEEN " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] +
            " AND " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]);
                break;
            }
        } catch(Exception e) {
            System.out.println("Err list period : "+e.toString());
        } finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
         
        public static String getStrVoucherPrev(java.util.Date transactionDate) {
        long periodId = getPerIdStartEntry(transactionDate);
        String whereClause = PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] + " = " + periodId;
        Vector vectCurrPeriod = PstActivityPeriod.list(0,0,whereClause,"");
        long currPeriodId = 0;
        String result = "";
        if(vectCurrPeriod!=null && vectCurrPeriod.size()>0){
            ActivityPeriod actPeriod = (ActivityPeriod)vectCurrPeriod.get(0);
            int intYear = actPeriod.getStartDate().getYear()+1900;
            int intMonth = actPeriod.getStartDate().getMonth()+1;
            String startDateYear = String.valueOf(intYear).substring(2);
            String startDateMonth = (String.valueOf(intMonth).length()==1) ? "0"+String.valueOf(intMonth) : String.valueOf(intMonth);
            result = startDateYear+startDateMonth;
        }
        return result;
    }
        
        public static String getStrVoucherEdit(String voucherNo){
        String result = "";
        StringTokenizer st = new StringTokenizer(voucherNo,"-",false);
        while(st.hasMoreTokens()){
            result = result + st.nextToken();
        }
        return result;
    }
        
        public static int getPeriodInterval(long periodeId) {
        int result = 1;
        String whereClause = PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] + " = " + periodeId;
        Vector vectPeriode = PstActivityPeriod.list(0, 0, whereClause, "");
        if (vectPeriode != null && vectPeriode.size() > 0) {
            ActivityPeriod actPeriodSelected = (ActivityPeriod) vectPeriode.get(0);
            java.util.Date startDate = actPeriodSelected.getStartDate();
            java.util.Date endDate = actPeriodSelected.getEndDate();
            result = endDate.getMonth() - startDate.getMonth() + 1;
        }
        return result;
    }
        
         public static ActivityPeriod getPeriodBetween(java.util.Date actualDate) {
        ActivityPeriod actPeriod = new ActivityPeriod();
        DBResultSet dbrs = null;
        ResultSet rs = null;
        try {
            java.sql.Date sqlDate = new java.sql.Date(actualDate.getTime());
            String sql = "SELECT * FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD
            + " WHERE '" + sqlDate
            + "' BETWEEN " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE]
            + " AND " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE];
            
            //System.out.println(sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            rs = dbrs.getResultSet();
            
            if (rs.next()) {
                actPeriod.setOID(rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]));
                actPeriod.setStartDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE]));
                actPeriod.setEndDate(rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE]));                
                actPeriod.setName(rs.getString(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_NAME]));
                actPeriod.setDescription(rs.getString(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_DESCRIPTION]));
                actPeriod.setPosted(rs.getInt(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_POSTED]));
            }
            
            
        } catch (Exception error) {
            System.out.println(".:: SessPeriode >> getPeriodBetween() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return actPeriod;
    }
         
         public static boolean isPeriodClosed(long periodeId){
        Vector tempPeriod = PstActivityPeriod.list(0,0," PERIODE_ID = "+periodeId,"");
        if(tempPeriod!=null && tempPeriod.size()>0){
            ActivityPeriod actPeriod = (ActivityPeriod)tempPeriod.get(0);
            if (actPeriod.getPosted() == PstActivityPeriod.PERIOD_CLOSED){
                return true;
            }   else{
                return false;
            }
        }
        return false;
    }
    
    /** this method used to get last period Id */
    public static Vector getLastPeriodId(){
        DBResultSet dbrs = null;
        Vector lists = new Vector(1,1);
        try{
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
            " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
            " WHERE " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_POSTED] + " = 1 " + //posted
            " ORDER BY " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE] +
            " DESC";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                ActivityPeriod actPeriod = new ActivityPeriod();
                actPeriod.setOID(rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]));
                lists.add(actPeriod);
            }
        }catch(Exception e){
            System.out.println("Err LastPeriod : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            
        }
        return lists;
    }
    
    /** This method used to list all period from before lastDate
     * @param lastDate --> specify lastDate
     */
    public static java.util.Date listStartDateOfPeriode(java.util.Date lastDate) {
        
        DBResultSet dbrs = null;
        java.util.Date result = null;
        String strDate = "\"" + Formater.formatDate(lastDate, "yyyy-MM-dd") + "\"";
        
        try{
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] +
            " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
            " WHERE " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] +
            " <= " + strDate + " AND " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE] +
            " >= " + strDate +
            " ORDER BY " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE] +
            " DESC";
            
            //System.out.println("--> SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = rs.getDate(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE]);
                break;
            }
        } catch(Exception e) {
            System.out.println("Err list period : "+e.toString());
        } finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    /** This method used to list all period from before lastDate
     * @param lastDate --> specify lastDate
     */
    
    
    /** this method used to get periodId just before current period */
    public static long getPeriodIdJustBefore(long currPeriodId){
        long result = 0;
        if(currPeriodId!=0){
            //Vector vectLastPeriod = SessPeriode.getLastPeriodId();
            String orderBy = PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_END_DATE] + " DESC";
            Vector vectLastPeriod = PstActivityPeriod.list(0,0,"",orderBy);
            //System.out.println("vectLastPeriod size : "+vectLastPeriod.size());
            if(vectLastPeriod!=null && vectLastPeriod.size()>1){
                for(int i=0; i<vectLastPeriod.size(); i++){
                    ActivityPeriod actPeriod = (ActivityPeriod)vectLastPeriod.get(i);
                    //System.out.println("per.getOID() : "+per.getOID());
                    if(currPeriodId==actPeriod.getOID() && i<vectLastPeriod.size()-1){
                        actPeriod = (ActivityPeriod)vectLastPeriod.get(i+1);
                        //System.out.println("return : "+per.getOID());
                        return actPeriod.getOID();
                    }
                }
            }
            
            if(vectLastPeriod.size()==1){
                ActivityPeriod actPeriod = (ActivityPeriod)vectLastPeriod.get(0);
                if(actPeriod.getOID()!=currPeriodId){
                    return actPeriod.getOID();
                }
            }
        }
        return result;
    }
    
    /**
     * this method used to get periodId for one year along specify by startDate and dueDate
     */
    public static String listPeriodbyStartDue(java.util.Date startDate,
    java.util.Date dueDate) {
        DBResultSet dbrs = null;
        String result = "";
        String strStartDate = "\"" + Formater.formatDate(startDate,"yyyy-MM-dd") + "\"";
        String strDueDate = "\"" + Formater.formatDate(dueDate,"yyyy-MM-dd") + "\"";
        try{
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
            " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
            " WHERE " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] +
            " BETWEEN " + strStartDate + " AND " + strDueDate;
            //System.out.println("---> SessPeriode.listPeriodbyStartDue() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = result + rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]) + ",";
            }
            if(result!="" && result.length()>0){result = result.substring(0,result.length()-1);}
        }catch(Exception e){
            System.out.println("---> SessPeriode.listPeriodbyStartDue() err : " + e.toString());
        }finally{
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    /**
     * this method used to get periodId for one year along specify by startDate and dueDate
     */
    public static Vector listPerBtwStartDueDate(java.util.Date startDate,
    java.util.Date dueDate){
        Vector result = new Vector(1,1);
        
        DBResultSet dbrs = null;
        String strStartDate = "\"" + Formater.formatDate(startDate,"yyyy-MM-dd") + "\"";
        String strDueDate = "\"" + Formater.formatDate(dueDate,"yyyy-MM-dd") + "\"";
        try{
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
            " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
            " WHERE " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] +
            " BETWEEN " + strStartDate +
            " AND " + strDueDate;
            //System.out.println("--->>> SessPeriode.listPerBtwStartDueDate() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result.add(String.valueOf(rs.getInt(1)));
            }
        }catch(Exception e){
            System.out.println("---eee SessPeriode.listPerBtwStartDueDate() err : " + e.toString());
        }finally{
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    /**
     * this method used to fetch periodId on specify year
     */
    public static long getFirstPeriodOnYear(java.util.Date selectedDate){
        DBResultSet dbrs = null;
        long result = 0;
        String strDate = "\"" + Formater.formatDate(selectedDate,"yyyy-MM-dd") + "\"";
        try{
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
            " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
            " WHERE YEAR(" + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] + ") " +
            " = YEAR(" + strDate + ")" +
            " ORDER BY " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE];
            //System.out.println("---> SessPeriode.getFirstPeriodOnYear() sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                result = rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]);
                break;
            }
        }catch(Exception e){
            System.out.println("---> SessPeriode.getFirstPeriodOnYear() err : " + e.toString());
        }finally{
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    

    // this method used to fetch periodId on specify year
    public static long getLastPeriodOidOnYear(java.util.Date selectedDate)
    {
        long result = 0;
        
        DBResultSet dbrs = null;        
        String sStartDate = "'" + Formater.formatDate(new java.util.Date(selectedDate.getYear(),0,1),"yyyy-MM-dd") + "'";
        String sEndDate = "'" + Formater.formatDate(new java.util.Date(selectedDate.getYear(),11,31),"yyyy-MM-dd") + "'";
        try
        {
            String sql = "SELECT " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID] +
                         " FROM " + PstActivityPeriod.TBL_ACTIVITY_PERIOD +
                         " WHERE " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] + 
                         " BETWEEN " + sStartDate + 
                         " AND " + sEndDate +
                         " ORDER BY " + PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_START_DATE] + 
                         " DESC";
            
//            System.out.println("getLastPeriodOidOnYear sql : " + sql); 
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next())
            {
                result = rs.getLong(PstActivityPeriod.fieldNames[PstActivityPeriod.FLD_ACTIVITY_PERIOD_ID]);
                break;
            }
        }
        catch(Exception e)
        {
            System.out.println("---> SessPeriode.getLastPeriodOidOnYear() err : " + e.toString());
        }
        finally
        {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
}
