/*
 * PstAccPayableDetail.java
 *
 * Created on May 4, 2007, 6:25 PM
 */

package com.dimata.posbo.entity.arap;

import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.Formater;
import com.dimata.posbo.db.*;

import java.util.Vector;
import java.sql.ResultSet;
import org.json.JSONObject;
/**
 *
 * @author  gwawan
 */
public class PstAccPayableDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    public static final String TBL_ACC_PAYABLE_DETAIL = "pos_acc_payable_detail";
    public static final int FLD_ACC_PAYABLE_DETAIL_ID = 0;
    public static final int FLD_ACC_PAYABLE_ID = 1;
    public static final int FLD_PAYMENT_SYSTEM_ID = 2;
    public static final int FLD_CURRENCY_TYPE_ID = 3;
    public static final int FLD_RATE = 4;
    public static final int FLD_AMOUNT = 5;
    
    public static String fieldNames[] = {
        "ACC_PAYABLE_DETAIL_ID",
        "ACC_PAYABLE_ID",
        "PAYMENT_SYSTEM_ID",
        "CURRENCY_TYPE_ID",
        "RATE",
        "AMOUNT"
    };
    
    public static int fieldTypes[] = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    /** Creates a new instance of PstAccPayableDetail */
    public PstAccPayableDetail() {
    }
    
    public PstAccPayableDetail(int i) throws DBException {
        super(new PstAccPayableDetail());
    }
    
    public PstAccPayableDetail(String sOid) throws DBException {
        super(new PstAccPayableDetail(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstAccPayableDetail(long lOid) throws DBException {
        super(new PstAccPayableDetail(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch(Exception e) {
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
        return this.getClass().getName();
    }
    
    public String getTableName() {
        return this.TBL_ACC_PAYABLE_DETAIL;
    }
    
    public long deleteExc(Entity ent) throws Exception {
        if(ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return this.deleteExc(ent.getOID());
    }
    
    public long fetchExc(Entity ent) throws Exception {
        AccPayableDetail accPayableDetail = fetchExc(ent.getOID());
        ent = (Entity)accPayableDetail;
        return accPayableDetail.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception {
        return this.insertExc((AccPayableDetail)ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return this.updateExc((AccPayableDetail)ent);
    }
    
    public static AccPayableDetail fetchExc(long oid) throws DBException {
        try {
            AccPayableDetail accPayableDetail = new AccPayableDetail();
            PstAccPayableDetail pstAccPayableDetail = new PstAccPayableDetail(oid);
            accPayableDetail.setOID(oid);
            
            accPayableDetail.setAccPayableId(pstAccPayableDetail.getlong(FLD_ACC_PAYABLE_ID));
            accPayableDetail.setPaymentSystemId(pstAccPayableDetail.getlong(FLD_PAYMENT_SYSTEM_ID));
            accPayableDetail.setCurrencyTypeId(pstAccPayableDetail.getlong(FLD_CURRENCY_TYPE_ID));
            accPayableDetail.setRate(pstAccPayableDetail.getdouble(FLD_RATE));
            accPayableDetail.setAmount(pstAccPayableDetail.getdouble(FLD_AMOUNT));
            
            return accPayableDetail;
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstAccPayableDetail(), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(AccPayableDetail accPayableDetail) throws DBException {
        try {
            PstAccPayableDetail pstAccPayableDetail = new PstAccPayableDetail(0);
            
            pstAccPayableDetail.setLong(FLD_ACC_PAYABLE_ID, accPayableDetail.getAccPayableId());
            pstAccPayableDetail.setLong(FLD_PAYMENT_SYSTEM_ID, accPayableDetail.getPaymentSystemId());
            pstAccPayableDetail.setLong(FLD_CURRENCY_TYPE_ID, accPayableDetail.getCurrencyTypeId());
            pstAccPayableDetail.setDouble(FLD_RATE, accPayableDetail.getRate());
            pstAccPayableDetail.setDouble(FLD_AMOUNT, accPayableDetail.getAmount());
            
            pstAccPayableDetail.insert();
            accPayableDetail.setOID(pstAccPayableDetail.getlong(FLD_ACC_PAYABLE_DETAIL_ID));
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstAccPayableDetail(0), DBException.UNKNOWN);
        }
        return accPayableDetail.getOID();
    }
    
    public static long updateExc(AccPayableDetail accPayableDetail) throws DBException {
        try {
            if(accPayableDetail != null && accPayableDetail.getOID() != 0) {
                PstAccPayableDetail pstAccPayableDetail = new PstAccPayableDetail(accPayableDetail.getOID());
                
                pstAccPayableDetail.setLong(FLD_ACC_PAYABLE_ID, accPayableDetail.getAccPayableId());
                pstAccPayableDetail.setLong(FLD_PAYMENT_SYSTEM_ID, accPayableDetail.getPaymentSystemId());
                pstAccPayableDetail.setLong(FLD_CURRENCY_TYPE_ID, accPayableDetail.getCurrencyTypeId());
                pstAccPayableDetail.setDouble(FLD_RATE, accPayableDetail.getRate());
                pstAccPayableDetail.setDouble(FLD_AMOUNT, accPayableDetail.getAmount());
                
                pstAccPayableDetail.update();
                return accPayableDetail.getOID();
            }
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstAccPayableDetail(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstAccPayableDetail pstAccPayableDetail = new PstAccPayableDetail(oid);
            pstAccPayableDetail.delete();
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstAccPayableDetail(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static void resultToObject(ResultSet rs, AccPayableDetail accPayableDetail) {
        try {
            accPayableDetail.setOID(rs.getLong(PstAccPayableDetail.fieldNames[FLD_ACC_PAYABLE_DETAIL_ID]));
            accPayableDetail.setAccPayableId(rs.getLong(PstAccPayableDetail.fieldNames[FLD_ACC_PAYABLE_ID]));
            accPayableDetail.setPaymentSystemId(rs.getLong(PstAccPayableDetail.fieldNames[FLD_PAYMENT_SYSTEM_ID]));
            accPayableDetail.setCurrencyTypeId(rs.getLong(PstAccPayableDetail.fieldNames[FLD_CURRENCY_TYPE_ID]));
            accPayableDetail.setRate(rs.getDouble(PstAccPayableDetail.fieldNames[FLD_RATE]));
            accPayableDetail.setAmount(rs.getDouble(PstAccPayableDetail.fieldNames[FLD_AMOUNT]));
        } catch(Exception e) {
            System.out.println("resultToObject: "+e.toString());
        }
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_ACC_PAYABLE_DETAIL + " ";
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
                AccPayableDetail accPayableDetail = new AccPayableDetail();
                resultToObject(rs, accPayableDetail);
                lists.add(accPayableDetail);
            }
        } catch (Exception e) {
            System.out.println(".:: " + new PstArApMain().getClass().getName() + ".list() : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstAccPayableDetail.fieldNames[FLD_ACC_PAYABLE_DETAIL_ID] + ") " +
                    " FROM " + TBL_ACC_PAYABLE_DETAIL;
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
            System.out.println(e.toString());
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         AccPayableDetail accPayableDetail = PstAccPayableDetail.fetchExc(oid);
         object.put(PstAccPayableDetail.fieldNames[PstAccPayableDetail.FLD_ACC_PAYABLE_DETAIL_ID], accPayableDetail.getOID());
         object.put(PstAccPayableDetail.fieldNames[PstAccPayableDetail.FLD_ACC_PAYABLE_ID], accPayableDetail.getAccPayableId());
         object.put(PstAccPayableDetail.fieldNames[PstAccPayableDetail.FLD_PAYMENT_SYSTEM_ID], accPayableDetail.getPaymentSystemId());
         object.put(PstAccPayableDetail.fieldNames[PstAccPayableDetail.FLD_CURRENCY_TYPE_ID], accPayableDetail.getCurrencyTypeId());
         object.put(PstAccPayableDetail.fieldNames[PstAccPayableDetail.FLD_RATE], accPayableDetail.getRate());
         object.put(PstAccPayableDetail.fieldNames[PstAccPayableDetail.FLD_AMOUNT], accPayableDetail.getAmount());
      }catch(Exception exc){}
      return object;
   }
}
