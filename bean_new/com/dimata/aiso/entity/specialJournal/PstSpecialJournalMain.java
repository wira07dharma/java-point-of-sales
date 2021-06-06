/*
 * PstSpecialJournalMain.java
 *
 * Created on February 6, 2007, 10:17 AM
 */

package com.dimata.aiso.entity.specialJournal;

/* import package aiso special journal */
import com.dimata.aiso.db.*;

/* import package qdep */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

/* import package interfaces */
import com.dimata.interfaces.journal.I_JournalType;

/* import package java */
import java.util.*;
import java.sql.*;


/**
 *
 * @author  dwi
 */
public class PstSpecialJournalMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_JournalType {
    
    public static final String TBL_AISO_SPC_JMAIN = "aiso_spc_jmain";
    
    public static final int FLD_JOURNAL_MAIN_ID = 0;
    public static final int FLD_ENTRY_DATE = 1;
    public static final int FLD_TRANS_DATE = 2;
    public static final int FLD_VOUCHER_NUMBER = 3;
    public static final int FLD_VOUCHER_COUNTER = 4;
    public static final int FLD_JOURNAL_NUMBER = 5;
    public static final int FLD_REFERENCE = 6;
    public static final int FLD_BOOK_TYPE = 7;
    public static final int FLD_AMOUNT = 8;
    public static final int FLD_AMOUNT_STATUS = 9;
    public static final int FLD_JOURNAL_STATUS = 10;
    public static final int FLD_BILYET_NUMBER = 11;
    public static final int FLD_BILYET_DUE_DATE = 12;
    public static final int FLD_ID_PERKIRAAN = 13;
    public static final int FLD_DEPARTMENT_ID = 14;
    public static final int FLD_PERIODE_ID = 15;
    public static final int FLD_USER_ID = 16;
    public static final int FLD_DESCRIPTION = 17;
    public static final int FLD_CONTACT_ID = 18;
    public static final int FLD_JOURNAL_TYPE = 19;
    public static final int FLD_CURRENCY_TYPE_ID = 20;
    public static final int FLD_STANDAR_RATE = 21;
    public static final int FLD_FR_CONTACT_ID = 22;
    
    public static String[] fieldNames = {
        "JOURNAL_MAIN_ID",
        "ENTRY_DATE",
        "TRANS_DATE",
        "VOUCHER_NUMBER",
        "VOUCHER_COUNTER",
        "JOURNAL_NUMBER",
        "REFERENCE",
        "BOOK_TYPE",
        "AMOUNT",
        "AMOUNT_STATUS",
        "JOURNAL_STATUS",
        "BILYET_NUMBER",
        "BILYET_DUE_DATE",
        "ID_PERKIRAAN",
        "DEPARTMENT_ID",
        "PERIODE_ID",
        "USER_ID",
        "DESCRIPTION",
        "CONTACT_ID",
        "JOURNAL_TYPE",
        "CURRENCY_TYPE_ID",
        "STANDAR_RATE",
        "FRCONTACT_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_ID + TYPE_PK + TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_FK + TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_LONG,
        TYPE_FK + TYPE_LONG,
        TYPE_STRING,
        TYPE_FK + TYPE_LONG,
        TYPE_INT,
        TYPE_FK + TYPE_LONG,
        TYPE_FLOAT,
        TYPE_LONG
    };
    
    public static final int DATASTATUS_CLEAN = 0;
    public static final int DATASTATUS_ADD = 1;
    public static final int DATASTATUS_UPDATE = 2;
    public static final int DATASTATUS_DELETE = 3;
    
    public static final int STS_DEBET = 1;
    public static final int STS_KREDIT = -1;
    
    /** Creates a new instance of PstSpecialJournalMain */
    public PstSpecialJournalMain() {
    }
    
    public PstSpecialJournalMain(int i) throws DBException {
        super(new PstSpecialJournalMain());
    }
     
    public PstSpecialJournalMain(String sOid) throws DBException {
        super(new PstSpecialJournalMain(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
      
    public PstSpecialJournalMain(long lOid) throws DBException {
        super(new PstSpecialJournalMain(0)); 
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
        return deleteExc((SpecialJournalMain)ent);
    }
    
    public long fetchExc(Entity ent) throws Exception {
        SpecialJournalMain objSpecialJournalMain = PstSpecialJournalMain.fetchExc(ent.getOID());
        ent = (Entity) objSpecialJournalMain;
        return ent.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception {
        return PstSpecialJournalMain.insertExc((SpecialJournalMain) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((SpecialJournalMain) ent);
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
        return new PstSpecialJournalMain().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_SPC_JMAIN;
    }
    
    public static SpecialJournalMain fetchExc(long lOid) throws DBException{
        try{
            SpecialJournalMain objSpecialJournalMain = new SpecialJournalMain();
            PstSpecialJournalMain pstSpecialJournalMain = new PstSpecialJournalMain(lOid);
            
            objSpecialJournalMain.setOID(lOid);
            objSpecialJournalMain.setEntryDate(pstSpecialJournalMain.getDate(FLD_ENTRY_DATE));
            objSpecialJournalMain.setTransDate(pstSpecialJournalMain.getDate(FLD_TRANS_DATE));
            objSpecialJournalMain.setVoucherNumber(pstSpecialJournalMain.getString(FLD_VOUCHER_NUMBER));
            objSpecialJournalMain.setVoucherCounter(pstSpecialJournalMain.getInt(FLD_VOUCHER_COUNTER));
            objSpecialJournalMain.setJournalNumber(pstSpecialJournalMain.getString(FLD_JOURNAL_NUMBER));
            objSpecialJournalMain.setReference(pstSpecialJournalMain.getString(FLD_REFERENCE));
            objSpecialJournalMain.setBookType(pstSpecialJournalMain.getInt(FLD_BOOK_TYPE));
            objSpecialJournalMain.setAmount(pstSpecialJournalMain.getdouble(FLD_AMOUNT));
            objSpecialJournalMain.setAmountStatus(pstSpecialJournalMain.getInt(FLD_AMOUNT_STATUS));
            objSpecialJournalMain.setJournalStatus(pstSpecialJournalMain.getInt(FLD_JOURNAL_STATUS));
            objSpecialJournalMain.setBilyetNumber(pstSpecialJournalMain.getString(FLD_BILYET_NUMBER));
            objSpecialJournalMain.setBilyetDueDate(pstSpecialJournalMain.getDate(FLD_BILYET_DUE_DATE));
            objSpecialJournalMain.setIdPerkiraan(pstSpecialJournalMain.getlong(FLD_ID_PERKIRAAN));
            objSpecialJournalMain.setDepartmentId(pstSpecialJournalMain.getlong(FLD_DEPARTMENT_ID));
            objSpecialJournalMain.setPeriodeId(pstSpecialJournalMain.getlong(FLD_PERIODE_ID));
            objSpecialJournalMain.setUserId(pstSpecialJournalMain.getlong(FLD_USER_ID));       
            objSpecialJournalMain.setDescription(pstSpecialJournalMain.getString(FLD_DESCRIPTION));
            objSpecialJournalMain.setContactId(pstSpecialJournalMain.getlong(FLD_CONTACT_ID));
            objSpecialJournalMain.setJournalType(pstSpecialJournalMain.getInt(FLD_JOURNAL_TYPE));
            objSpecialJournalMain.setCurrencyTypeId(pstSpecialJournalMain.getlong(FLD_CURRENCY_TYPE_ID));
            objSpecialJournalMain.setStandarRate(pstSpecialJournalMain.getdouble(FLD_STANDAR_RATE));
            objSpecialJournalMain.setFrcontactId(pstSpecialJournalMain.getlong(FLD_FR_CONTACT_ID));
            
            return objSpecialJournalMain;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            System.out.println("sadfasdasdas "+e.toString());
            throw new DBException(new PstSpecialJournalMain(0), DBException.UNKNOWN);
        }       
    }
    
    public static long insertExc(SpecialJournalMain objSpecialJournalMain) throws DBException{
        try{
            PstSpecialJournalMain pstSpecialJournalMain = new PstSpecialJournalMain(0);
            
            pstSpecialJournalMain.setDate(FLD_ENTRY_DATE, objSpecialJournalMain.getEntryDate());
            pstSpecialJournalMain.setDate(FLD_TRANS_DATE, objSpecialJournalMain.getTransDate());
            pstSpecialJournalMain.setString(FLD_VOUCHER_NUMBER, objSpecialJournalMain.getVoucherNumber());
            pstSpecialJournalMain.setInt(FLD_VOUCHER_COUNTER, objSpecialJournalMain.getVoucherCounter());
            pstSpecialJournalMain.setString(FLD_JOURNAL_NUMBER, objSpecialJournalMain.getJournalNumber());
            pstSpecialJournalMain.setString(FLD_REFERENCE, objSpecialJournalMain.getReference());
            pstSpecialJournalMain.setInt(FLD_BOOK_TYPE, objSpecialJournalMain.getBookType());
            pstSpecialJournalMain.setDouble(FLD_AMOUNT, objSpecialJournalMain.getAmount());
            pstSpecialJournalMain.setInt(FLD_AMOUNT_STATUS, objSpecialJournalMain.getAmountStatus());
            pstSpecialJournalMain.setInt(FLD_JOURNAL_STATUS, objSpecialJournalMain.getJournalStatus());
            pstSpecialJournalMain.setString(FLD_BILYET_NUMBER, objSpecialJournalMain.getBilyetNumber());
            pstSpecialJournalMain.setDate(FLD_BILYET_DUE_DATE, objSpecialJournalMain.getBilyetDueDate());
            pstSpecialJournalMain.setLong(FLD_ID_PERKIRAAN, objSpecialJournalMain.getIdPerkiraan());
            pstSpecialJournalMain.setLong(FLD_DEPARTMENT_ID, objSpecialJournalMain.getDepartmentId());
            pstSpecialJournalMain.setLong(FLD_PERIODE_ID, objSpecialJournalMain.getPeriodeId());
            pstSpecialJournalMain.setLong(FLD_USER_ID, objSpecialJournalMain.getUserId());
            pstSpecialJournalMain.setString(FLD_DESCRIPTION, objSpecialJournalMain.getDescription());
            pstSpecialJournalMain.setLong(FLD_CONTACT_ID, objSpecialJournalMain.getContactId());
            pstSpecialJournalMain.setInt(FLD_JOURNAL_TYPE, objSpecialJournalMain.getJournalType());
            pstSpecialJournalMain.setLong(FLD_CURRENCY_TYPE_ID, objSpecialJournalMain.getCurrencyTypeId());
            pstSpecialJournalMain.setDouble(FLD_STANDAR_RATE, objSpecialJournalMain.getStandarRate());
            pstSpecialJournalMain.setLong(FLD_FR_CONTACT_ID, objSpecialJournalMain.getFrcontactId());
            
            System.out.println("objSpecialJournalMain.getStandarRate() di PstJournalMain =====> "+objSpecialJournalMain.getStandarRate());
            pstSpecialJournalMain.insert();
            objSpecialJournalMain.setOID(pstSpecialJournalMain.getlong(FLD_JOURNAL_MAIN_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalMain(0), DBException.UNKNOWN);
        }
        return objSpecialJournalMain.getOID();
    }
    
    public static long updateExc(SpecialJournalMain objSpecialJournalMain) throws DBException{
        try{
            if(objSpecialJournalMain.getOID() != 0){
                PstSpecialJournalMain pstSpecialJournalMain = new PstSpecialJournalMain(objSpecialJournalMain.getOID());
                
                pstSpecialJournalMain.setDate(FLD_ENTRY_DATE, objSpecialJournalMain.getEntryDate());
                pstSpecialJournalMain.setDate(FLD_TRANS_DATE, objSpecialJournalMain.getTransDate());
                pstSpecialJournalMain.setString(FLD_VOUCHER_NUMBER, objSpecialJournalMain.getVoucherNumber());
                pstSpecialJournalMain.setInt(FLD_VOUCHER_COUNTER, objSpecialJournalMain.getVoucherCounter());
                pstSpecialJournalMain.setString(FLD_JOURNAL_NUMBER, objSpecialJournalMain.getJournalNumber());
                pstSpecialJournalMain.setString(FLD_REFERENCE, objSpecialJournalMain.getReference());
                pstSpecialJournalMain.setInt(FLD_BOOK_TYPE, objSpecialJournalMain.getBookType());
                pstSpecialJournalMain.setDouble(FLD_AMOUNT, objSpecialJournalMain.getAmount());                
                pstSpecialJournalMain.setInt(FLD_AMOUNT_STATUS, objSpecialJournalMain.getAmountStatus());
                pstSpecialJournalMain.setInt(FLD_JOURNAL_STATUS, objSpecialJournalMain.getJournalStatus());
                pstSpecialJournalMain.setString(FLD_BILYET_NUMBER, objSpecialJournalMain.getBilyetNumber());
                pstSpecialJournalMain.setDate(FLD_BILYET_DUE_DATE, objSpecialJournalMain.getBilyetDueDate());
                pstSpecialJournalMain.setLong(FLD_ID_PERKIRAAN, objSpecialJournalMain.getIdPerkiraan());
                pstSpecialJournalMain.setLong(FLD_DEPARTMENT_ID, objSpecialJournalMain.getDepartmentId());
                pstSpecialJournalMain.setLong(FLD_PERIODE_ID, objSpecialJournalMain.getPeriodeId());
                pstSpecialJournalMain.setLong(FLD_USER_ID, objSpecialJournalMain.getUserId());
                pstSpecialJournalMain.setString(FLD_DESCRIPTION, objSpecialJournalMain.getDescription());
                pstSpecialJournalMain.setLong(FLD_CONTACT_ID, objSpecialJournalMain.getContactId());
                pstSpecialJournalMain.setInt(FLD_JOURNAL_TYPE, objSpecialJournalMain.getJournalType());
                pstSpecialJournalMain.setLong(FLD_CURRENCY_TYPE_ID, objSpecialJournalMain.getCurrencyTypeId());
                pstSpecialJournalMain.setDouble(FLD_STANDAR_RATE, objSpecialJournalMain.getStandarRate());
                pstSpecialJournalMain.setLong(FLD_FR_CONTACT_ID, objSpecialJournalMain.getFrcontactId());
            
                pstSpecialJournalMain.update();
                return objSpecialJournalMain.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalMain(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstSpecialJournalMain pstSpecialJournalMain = new PstSpecialJournalMain(lOid);            
            pstSpecialJournalMain.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSpecialJournalMain(0), DBException.UNKNOWN);
        }
        return lOid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
		DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_SPC_JMAIN + " ";

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
            System.out.println("Sql list special jurnal main ::::: "+sql);

            while (rs.next()) {
                SpecialJournalMain objSpecialJournalMain = new SpecialJournalMain();
                resultToObject(rs, objSpecialJournalMain);
                lists.add(objSpecialJournalMain);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(new PstSpecialJournalMain().getClass().getName() + ".list() exc : " + e.toString());
        }
        return new Vector();
    }
    
    public static void resultToObject(ResultSet rs, SpecialJournalMain objSpecialJournalMain){
        try{
            objSpecialJournalMain.setOID(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID]));
            objSpecialJournalMain.setEntryDate(rs.getDate(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ENTRY_DATE]));
            objSpecialJournalMain.setTransDate(rs.getDate(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_TRANS_DATE]));
            objSpecialJournalMain.setVoucherNumber(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_NUMBER]));
            objSpecialJournalMain.setVoucherCounter(rs.getInt(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_VOUCHER_COUNTER]));
            objSpecialJournalMain.setJournalNumber(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_NUMBER]));
            objSpecialJournalMain.setReference(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_REFERENCE]));
            objSpecialJournalMain.setBookType(rs.getInt(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_BOOK_TYPE]));
            objSpecialJournalMain.setAmount(rs.getDouble(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT]));
            objSpecialJournalMain.setAmountStatus(rs.getInt(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_AMOUNT_STATUS]));
            objSpecialJournalMain.setJournalStatus(rs.getInt(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_STATUS]));
            objSpecialJournalMain.setBilyetNumber(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_STATUS]));
            objSpecialJournalMain.setBilyetDueDate(rs.getDate(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_BILYET_DUE_DATE]));
            objSpecialJournalMain.setIdPerkiraan(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_ID_PERKIRAAN]));
            objSpecialJournalMain.setDepartmentId(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DEPARTMENT_ID]));
            objSpecialJournalMain.setPeriodeId(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID]));
            objSpecialJournalMain.setUserId(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_USER_ID]));
            objSpecialJournalMain.setDescription(rs.getString(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_DESCRIPTION]));
            objSpecialJournalMain.setContactId(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_CONTACT_ID]));
            objSpecialJournalMain.setJournalType(rs.getInt(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_TYPE]));
            objSpecialJournalMain.setCurrencyTypeId(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_CURRENCY_TYPE_ID]));
            objSpecialJournalMain.setStandarRate(rs.getDouble(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_STANDAR_RATE]));
            objSpecialJournalMain.setFrcontactId(rs.getLong(PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_FR_CONTACT_ID]));
            
        }catch(Exception e){
            System.out.println("Exception pd result to object ====> "+ e.toString());
        }
    
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] + ") " +
                    " FROM " + TBL_AISO_SPC_JMAIN;
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
    
    public static Vector getVectJournalId(long periodId) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT " + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_JOURNAL_MAIN_ID] +
                    " FROM " + PstSpecialJournalMain.TBL_AISO_SPC_JMAIN +
                    " WHERE " + PstSpecialJournalMain.fieldNames[PstSpecialJournalMain.FLD_PERIODE_ID] +
                    " = " + periodId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result.add(String.valueOf(rs.getLong(1)));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * temporary method used to delete journal periodic
     */
   /* public static void deleteJournalPeriod(long periodId) {
        Vector result = getVectJournalId(periodId);
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                long oid = Long.parseLong(String.valueOf(result.get(i)));
                try {
                    PstJurnalDetail.deleteByJurnalIDExc(oid);
                    PstSpecialJournalMain.deleteExc(oid);
                } catch (Exception e) {
                    System.out.println("Err : " + e.toString());
                }
            }
        }
    }*/
}
