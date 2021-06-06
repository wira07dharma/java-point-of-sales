/*
 * PstSpecialJournalDetailAssignt.java
 *
 * Created on February 9, 2007, 9:54 AM
 */

package com.dimata.aiso.entity.specialJournal;

/* import package java util */
import java.util.*;

/* import package aiso */
import com.dimata.aiso.db.*;

/* import package qdep */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

/* import package java sql */
import java.sql.*;

/**
 *
 * @author  dwi
 */
public class PstSpecialJournalDetailAssignt extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_AISO_JDETAIL_ASSIGNT = "aiso_jdetail_assignt";
    
    public static final int FLD_JDETAIL_ASSIGNT_ID = 0;
    public static final int FLD_AMOUNT = 1;
    public static final int FLD_SHARE_PROCENTAGE = 2;
    public static final int FLD_JOURNAL_DETAIL_ID = 3;
    public static final int FLD_ACTIVITY_ID = 4;
    
    public static String[] fieldNames = {
        "JDETAIL_ASSIGNT_ID",
        "AMOUNT",
        "SHARE_PROCENTAGE",
        "JOURNAL_DETAIL_ID",
        "ACTIVITY_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_ID + TYPE_PK + TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG
    };
    
    public static final int DATASTATUS_CLEAN = 0;
    public static final int DATASTATUS_ADD = 1;
    public static final int DATASTATUS_UPDATE = 2;
    public static final int DATASTATUS_DELETE = 3;
    
    /** Creates a new instance of PstSpecialJournalDetailAssignt */
    public PstSpecialJournalDetailAssignt() {
    }
    
    public PstSpecialJournalDetailAssignt(int i) throws DBException {
        super(new PstSpecialJournalDetailAssignt());
    }
    
    public PstSpecialJournalDetailAssignt(String sOid) throws DBException {
        super(new PstSpecialJournalDetailAssignt(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstSpecialJournalDetailAssignt(long lOid) throws DBException {
        super(new PstSpecialJournalDetailAssignt(0)); 
        String sOid = "";
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
        return deleteExc((SpecialJournalDetailAssignt)ent);
    }
    
    public long fetchExc(Entity ent) throws Exception {
        SpecialJournalDetailAssignt objSpecialJournalDetailAssignt = PstSpecialJournalDetailAssignt.fetchExc(ent.getOID());
        ent = (Entity)objSpecialJournalDetailAssignt;
        return objSpecialJournalDetailAssignt.getOID();
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
        return new PstSpecialJournalDetailAssignt().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_JDETAIL_ASSIGNT;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((SpecialJournalDetailAssignt)ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((SpecialJournalDetailAssignt)ent);
    }
    
    public static SpecialJournalDetailAssignt fetchExc(long lOid) throws DBException{
        try{
            SpecialJournalDetailAssignt objSpecialJournalDetailAssignt = new SpecialJournalDetailAssignt();
            PstSpecialJournalDetailAssignt pstSpecialJournalDetailAssignt = new PstSpecialJournalDetailAssignt(lOid);
            
            objSpecialJournalDetailAssignt.setOID(lOid);
            objSpecialJournalDetailAssignt.setAmount(pstSpecialJournalDetailAssignt.getdouble(FLD_AMOUNT));
            objSpecialJournalDetailAssignt.setShareProcentage(pstSpecialJournalDetailAssignt.getfloat(FLD_SHARE_PROCENTAGE));
            objSpecialJournalDetailAssignt.setJournalDetailId(pstSpecialJournalDetailAssignt.getlong(FLD_JOURNAL_DETAIL_ID));
            objSpecialJournalDetailAssignt.setActivityId(pstSpecialJournalDetailAssignt.getlong(FLD_ACTIVITY_ID));
            
             return objSpecialJournalDetailAssignt;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalDetailAssignt(0), DBException.UNKNOWN);
        }       
    }
    
    public static long insertExc(SpecialJournalDetailAssignt objSpecialJournalDetailAssignt) throws DBException{
        try{
            PstSpecialJournalDetailAssignt pstSpecialJournalDetailAssignt = new PstSpecialJournalDetailAssignt(0);
            
            pstSpecialJournalDetailAssignt.setDouble(FLD_AMOUNT, objSpecialJournalDetailAssignt.getAmount());
            pstSpecialJournalDetailAssignt.setFloat(FLD_SHARE_PROCENTAGE, objSpecialJournalDetailAssignt.getShareProcentage());
            pstSpecialJournalDetailAssignt.setLong(FLD_JOURNAL_DETAIL_ID, objSpecialJournalDetailAssignt.getJournalDetailId());
            pstSpecialJournalDetailAssignt.setLong(FLD_ACTIVITY_ID, objSpecialJournalDetailAssignt.getActivityId());
            
            pstSpecialJournalDetailAssignt.insert();
            objSpecialJournalDetailAssignt.setOID(pstSpecialJournalDetailAssignt.getlong(FLD_JDETAIL_ASSIGNT_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalDetailAssignt(0), DBException.UNKNOWN);
        }
        return objSpecialJournalDetailAssignt.getOID();
    }
    
    public static long updateExc(SpecialJournalDetailAssignt objSpecialJournalDetailAssignt) throws DBException{
        try{
            if(objSpecialJournalDetailAssignt.getOID() != 0){
                PstSpecialJournalDetailAssignt pstSpecialJournalDetailAssignt = new PstSpecialJournalDetailAssignt(objSpecialJournalDetailAssignt.getOID());
                
                pstSpecialJournalDetailAssignt.setDouble(FLD_AMOUNT, objSpecialJournalDetailAssignt.getAmount());
                pstSpecialJournalDetailAssignt.setFloat(FLD_SHARE_PROCENTAGE, objSpecialJournalDetailAssignt.getShareProcentage());
                pstSpecialJournalDetailAssignt.setLong(FLD_JOURNAL_DETAIL_ID, objSpecialJournalDetailAssignt.getJournalDetailId());
                pstSpecialJournalDetailAssignt.setLong(FLD_ACTIVITY_ID, objSpecialJournalDetailAssignt.getActivityId());
                
                pstSpecialJournalDetailAssignt.update();                
                objSpecialJournalDetailAssignt.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalDetailAssignt(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid)throws DBException{
        try{
            PstSpecialJournalDetailAssignt pstSpecialJournalDetailAssignt = new PstSpecialJournalDetailAssignt(lOid);
            pstSpecialJournalDetailAssignt.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalDetailAssignt(0), DBException.UNKNOWN);
        }
        return lOid;
    }  
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {

            String sql = "SELECT * FROM " + TBL_AISO_JDETAIL_ASSIGNT + " ";

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

            while (rs.next()) {
                SpecialJournalDetailAssignt objSpecialJournalDetailAssignt = new SpecialJournalDetailAssignt();
                resultToObject(rs, objSpecialJournalDetailAssignt);
                lists.add(objSpecialJournalDetailAssignt);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println("Exc when list special jurnal detail assignt : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static void resultToObject(ResultSet rs, SpecialJournalDetailAssignt objSpecialJournalDetailAssignt){
        try{
            
            objSpecialJournalDetailAssignt.setAmount(rs.getDouble(PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_AMOUNT]));
            objSpecialJournalDetailAssignt.setShareProcentage(rs.getFloat(PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_SHARE_PROCENTAGE]));
            objSpecialJournalDetailAssignt.setActivityId(rs.getLong(PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_ACTIVITY_ID]));
            objSpecialJournalDetailAssignt.setJournalDetailId(rs.getLong(PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JOURNAL_DETAIL_ID]));
            objSpecialJournalDetailAssignt.setOID(rs.getLong(PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JDETAIL_ASSIGNT_ID]));
            
        }catch(Exception e){
            System.out.println("Exception on resultToObject special journal detail assign ::: "+e.toString());
        }
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JDETAIL_ASSIGNT_ID] + ") FROM " +
                    TBL_AISO_JDETAIL_ASSIGNT;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;

        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static long deleteByJurnalIDExc(long oidJurnalMain) throws DBException {
        if (oidJurnalMain > 0) {
            long oidResult = 0;
            String strWhereClause = PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JDETAIL_ASSIGNT_ID] + " = " + oidJurnalMain;
            Vector listJurDetail = PstSpecialJournalDetailAssignt.list(0, 0, strWhereClause, "");
            if (listJurDetail != null && listJurDetail.size() > 0) {
                for (int i = 0; i < listJurDetail.size(); i++) {
                    SpecialJournalDetailAssignt objSpecialJournalDetailAssignt = (SpecialJournalDetailAssignt) listJurDetail.get(i);
                    oidResult = PstSpecialJournalDetailAssignt.deleteExc(objSpecialJournalDetailAssignt.getOID());
                }
            }
            return oidResult;
        } else {
            return 0;
        }
    }
     
    public static void deleteByJurnalIDDetail(long oidJurnalDetail) throws DBException {
        if (oidJurnalDetail > 0) {
            long oidResult = 0;
            String sql = "delete from "+TBL_AISO_JDETAIL_ASSIGNT;
            sql = sql + " where "+PstSpecialJournalDetailAssignt.fieldNames[PstSpecialJournalDetailAssignt.FLD_JOURNAL_DETAIL_ID] + " = " + oidJurnalDetail;
            System.out.println("delete : "+sql);
            DBHandler.execUpdate(sql);    
        }
    }

    public boolean isDetailItemClear(Vector vectJurnalDetail) {
        if (vectJurnalDetail != null && vectJurnalDetail.size() > 0) {
            for (int i = 0; i < vectJurnalDetail.size(); i++) {
                SpecialJournalDetailAssignt objSpecialJournalDetailAssignt = (SpecialJournalDetailAssignt) vectJurnalDetail.get(i);
                if (objSpecialJournalDetailAssignt.getDataStatus() != PstSpecialJournalDetailAssignt.DATASTATUS_CLEAN) { 
                    return false;
                }
            }
        }
        return true;
    }
}
