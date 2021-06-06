/*
 * PstSpecialJournalDetail.java
 *
 * Created on February 6, 2007, 3:46 PM
 */

package com.dimata.aiso.entity.specialJournal;

/* import package aiso db */
import com.dimata.aiso.db.*;

/* import package qdep entity */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

/* import package java util */
import java.util.*;

/* import package java sql */
import java.sql.*;


/**
 *
 * @author  dwi
 */
public class PstSpecialJournalDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_AISO_SPC_JDETAIL = "aiso_spc_jdetail";
    
    public static final int FLD_JOURNAL_DETAIL_ID = 0;
    public static final int FLD_DESCRIPTION = 1;
    public static final int FLD_AMOUNT = 2;
    public static final int FLD_AMOUNT_STATUS = 3;
    public static final int FLD_CURRENCY_RATE = 4;
    public static final int FLD_JOURNAL_MAIN_ID = 5;
    public static final int FLD_ID_PERKIRAAN = 6;
    public static final int FLD_CONTACT_ID = 7;
    public static final int FLD_CURRENCY_TYPE_ID = 8;
    public static final int FLD_DEPARTMENT_ID = 9;
    public static final int FLD_BUDGET_BALANCE = 10;
    
    public static String[] fieldNames = {
        "JOURNAL_DETAIL_ID",
        "DESCRIPTION",
        "AMOUNT",
        "AMOUNT_STATUS",
        "CURRENCY_RATE",
        "JOURNAL_MAIN_ID",
        "ID_PERKIRAAN",
        "CONTACT_ID",
        "CURRENCY_TYPE_ID",
        "DEPARTMENT_ID",
        "BUDGET_BALANCE"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_FLOAT
    };
    
    public static final int DATASTATUS_CLEAN = 0;
    public static final int DATASTATUS_ADD = 1;
    public static final int DATASTATUS_UPDATE = 2;
    public static final int DATASTATUS_DELETE = 3;

    public static final int SIDE_DEBET = 1;
    public static final int SIDE_CREDIT = -1;
    
    public PstSpecialJournalDetail() {
    }
    
    public PstSpecialJournalDetail(int i) throws DBException {
        super(new PstSpecialJournalDetail());
    }
    
    public PstSpecialJournalDetail(String sOid) throws DBException{
        super(new PstSpecialJournalDetail(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstSpecialJournalDetail(long lOid) throws DBException{
        super(new PstSpecialJournalDetail(0));
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
        return deleteExc((SpecialJournalDetail) ent);
    }
    
    public long fetchExc(Entity ent) throws Exception {
        SpecialJournalDetail objSpecialJournalDetail = PstSpecialJournalDetail.fetchExc(ent.getOID());
        ent = (Entity)objSpecialJournalDetail;
        return objSpecialJournalDetail.getOID();
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
        return new PstSpecialJournalDetail().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_SPC_JDETAIL;
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((SpecialJournalDetail)ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((SpecialJournalDetail)ent);
    }
    
    public static SpecialJournalDetail fetchExc(long lOid) throws DBException{
        try{
            SpecialJournalDetail objSpecialJournalDetail = new SpecialJournalDetail();
            PstSpecialJournalDetail pstSpecialJournalDetail = new PstSpecialJournalDetail(lOid);
            
            objSpecialJournalDetail.setOID(lOid);
            objSpecialJournalDetail.setDescription(pstSpecialJournalDetail.getString(FLD_DESCRIPTION));
            objSpecialJournalDetail.setAmount(pstSpecialJournalDetail.getdouble(FLD_AMOUNT));
            objSpecialJournalDetail.setAmountStatus(pstSpecialJournalDetail.getInt(FLD_AMOUNT_STATUS));
            objSpecialJournalDetail.setCurrencyRate(pstSpecialJournalDetail.getdouble(FLD_CURRENCY_RATE));
            objSpecialJournalDetail.setJournalMainId(pstSpecialJournalDetail.getlong(FLD_JOURNAL_MAIN_ID));
            objSpecialJournalDetail.setIdPerkiraan(pstSpecialJournalDetail.getlong(FLD_ID_PERKIRAAN));
            objSpecialJournalDetail.setCurrencyTypeId(pstSpecialJournalDetail.getlong(FLD_CURRENCY_TYPE_ID));
            objSpecialJournalDetail.setDepartmentId(pstSpecialJournalDetail.getlong(FLD_DEPARTMENT_ID));
            objSpecialJournalDetail.setContactId(pstSpecialJournalDetail.getlong(FLD_CONTACT_ID));
            objSpecialJournalDetail.setBudgetBalance(pstSpecialJournalDetail.getdouble(FLD_BUDGET_BALANCE));
            
            return objSpecialJournalDetail;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalDetail(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(SpecialJournalDetail objSpecialJournalDetail) throws DBException{
        try{
            PstSpecialJournalDetail pstSpecialJournalDetail = new PstSpecialJournalDetail(0);            
           
            pstSpecialJournalDetail.setString(FLD_DESCRIPTION,  objSpecialJournalDetail.getDescription());
            pstSpecialJournalDetail.setDouble(FLD_AMOUNT, objSpecialJournalDetail.getAmount());
            pstSpecialJournalDetail.setInt(FLD_AMOUNT_STATUS, objSpecialJournalDetail.getAmountStatus());
            pstSpecialJournalDetail.setDouble(FLD_CURRENCY_RATE, objSpecialJournalDetail.getCurrencyRate());
            pstSpecialJournalDetail.setLong(FLD_JOURNAL_MAIN_ID, objSpecialJournalDetail.getJournalMainId());
            pstSpecialJournalDetail.setLong(FLD_ID_PERKIRAAN, objSpecialJournalDetail.getIdPerkiraan());
            pstSpecialJournalDetail.setLong(FLD_CURRENCY_TYPE_ID, objSpecialJournalDetail.getCurrencyTypeId());
            pstSpecialJournalDetail.setLong(FLD_DEPARTMENT_ID, objSpecialJournalDetail.getDepartmentId());
            pstSpecialJournalDetail.setLong(FLD_CONTACT_ID, objSpecialJournalDetail.getContactId());
            pstSpecialJournalDetail.setDouble(FLD_BUDGET_BALANCE, objSpecialJournalDetail.getBudgetBalance());
            
            pstSpecialJournalDetail.insert();
            objSpecialJournalDetail.setOID(pstSpecialJournalDetail.getlong(FLD_JOURNAL_DETAIL_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalDetail(0), DBException.UNKNOWN);
        }
        return objSpecialJournalDetail.getOID();
    }
    
    public static long updateExc(SpecialJournalDetail objSpecialJournalDetail) throws DBException{
        try{
            if(objSpecialJournalDetail.getOID() != 0){
                PstSpecialJournalDetail pstSpecialJournalDetail = new PstSpecialJournalDetail(objSpecialJournalDetail.getOID());
                
                pstSpecialJournalDetail.setString(FLD_DESCRIPTION, objSpecialJournalDetail.getDescription());
                pstSpecialJournalDetail.setDouble(FLD_AMOUNT, objSpecialJournalDetail.getAmount());
                pstSpecialJournalDetail.setInt(FLD_AMOUNT_STATUS, objSpecialJournalDetail.getAmountStatus());
                pstSpecialJournalDetail.setDouble(FLD_CURRENCY_RATE, objSpecialJournalDetail.getCurrencyRate());
                pstSpecialJournalDetail.setLong(FLD_JOURNAL_MAIN_ID, objSpecialJournalDetail.getJournalMainId());
                pstSpecialJournalDetail.setLong(FLD_ID_PERKIRAAN, objSpecialJournalDetail.getIdPerkiraan());
                pstSpecialJournalDetail.setLong(FLD_CURRENCY_TYPE_ID, objSpecialJournalDetail.getCurrencyTypeId());
                pstSpecialJournalDetail.setLong(FLD_DEPARTMENT_ID, objSpecialJournalDetail.getDepartmentId());
                pstSpecialJournalDetail.setLong(FLD_CONTACT_ID, objSpecialJournalDetail.getContactId());
                pstSpecialJournalDetail.setDouble(FLD_BUDGET_BALANCE, objSpecialJournalDetail.getBudgetBalance());
                
                pstSpecialJournalDetail.update();
                objSpecialJournalDetail.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalDetail(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstSpecialJournalDetail pstSpecialJournalDetail = new PstSpecialJournalDetail(lOid);
            pstSpecialJournalDetail.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalDetail(0), DBException.UNKNOWN);
        }
        return lOid;
    }
    
     public static Vector listAll() {
        return list(0, 0, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {

            String sql = "SELECT * FROM " + TBL_AISO_SPC_JDETAIL + " ";

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
                SpecialJournalDetail objSpecialJournalDetail = new SpecialJournalDetail();
                resultToObject(rs, objSpecialJournalDetail);
                lists.add(objSpecialJournalDetail);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println("Exc when list special jurnal detail : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static void resultToObject(ResultSet rs, SpecialJournalDetail objSpecialJournalDetail){
        try{
            objSpecialJournalDetail.setOID(rs.getLong(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID]));
            objSpecialJournalDetail.setDescription(rs.getString(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_DESCRIPTION]));
            objSpecialJournalDetail.setAmount(rs.getDouble(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT]));
            objSpecialJournalDetail.setAmountStatus(rs.getInt(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_AMOUNT_STATUS]));
            objSpecialJournalDetail.setCurrencyRate(rs.getDouble(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_CURRENCY_RATE]));
            objSpecialJournalDetail.setJournalMainId(rs.getLong(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID]));
            objSpecialJournalDetail.setIdPerkiraan(rs.getLong(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_ID_PERKIRAAN]));
            objSpecialJournalDetail.setCurrencyTypeId(rs.getLong(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_CURRENCY_TYPE_ID]));
            objSpecialJournalDetail.setDepartmentId(rs.getLong(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_DEPARTMENT_ID]));
            objSpecialJournalDetail.setContactId(rs.getLong(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_CONTACT_ID]));
            objSpecialJournalDetail.setBudgetBalance(rs.getDouble(PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_BUDGET_BALANCE]));
            
        }catch(Exception e){
            System.out.println("Exception on resultToObject ===> "+e.toString());
        }    
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_DETAIL_ID] + ") FROM " +
                    TBL_AISO_SPC_JDETAIL;
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
            String strWhereClause = PstSpecialJournalDetail.fieldNames[PstSpecialJournalDetail.FLD_JOURNAL_MAIN_ID] + " = " + oidJurnalMain;
            Vector listJurDetail = PstSpecialJournalDetail.list(0, 0, strWhereClause, "");
            if (listJurDetail != null && listJurDetail.size() > 0) {
                for (int i = 0; i < listJurDetail.size(); i++) {
                    SpecialJournalDetail objSpecialJournalDetail = (SpecialJournalDetail) listJurDetail.get(i);
                    PstSpecialJournalDetailAssignt.deleteByJurnalIDDetail(objSpecialJournalDetail.getOID());
                    oidResult = PstSpecialJournalDetail.deleteExc(objSpecialJournalDetail.getOID());
                }
            }
            return oidResult;
        } else {
            return 0;
        }
    }
     
     public boolean isDetailItemClear(Vector vectJurnalDetail) {
        if (vectJurnalDetail != null && vectJurnalDetail.size() > 0) {
            for (int i = 0; i < vectJurnalDetail.size(); i++) {
                SpecialJournalDetail objSpecialJournalDetail = (SpecialJournalDetail) vectJurnalDetail.get(i);
                if (objSpecialJournalDetail.getDataStatus() != PstSpecialJournalDetail.DATASTATUS_CLEAN) {
                    return false;
                }
            }
        }
        return true;
    }
}
