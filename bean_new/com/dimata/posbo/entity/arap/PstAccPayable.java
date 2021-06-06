/*
 * PstAccPayable.java
 *
 * Created on May 4, 2007, 6:23 PM
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
public class PstAccPayable extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    public static final String TBL_ACC_PAYABLE = "pos_acc_payable";
    public static final int FLD_ACC_PAYABLE_ID = 0;
    public static final int FLD_RECEIVE_MATERIAL_ID = 1;
    public static final int FLD_PAYMENT_DATE = 2;
    public static final int FLD_DESCRIPTION = 3;
    public static final int FLD_NUM_OF_PAYMENT = 4;
    public static final int FLD_STATUS = 5;
    public static final int FLD_PAYMENT_NUMBER=6;
    public static final int FLD_NO_URUT=7;
    
    public static String fieldNames[] = {
        "ACC_PAYABLE_ID",
        "RECEIVE_MATERIAL_ID",
        "PAYMENT_DATE",
        "DESCRIPTION",
        "NUM_OF_PAYMENT",
        "STATUS",
        "PAYMENT_NUMBER",
        "NO_URUT"
           
    };
    
    public static int fieldTypes[] = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT
    };
    
    /** Creates a new instance of PstAccPayable */
    public PstAccPayable() {
    }
    
    public PstAccPayable(int i) throws DBException {
        super(new PstAccPayable());
    }
    
    public PstAccPayable(String sOid) throws DBException {
        super(new PstAccPayable(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstAccPayable(long lOid) throws DBException {
        super(new PstAccPayable(0));
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
        return this.fieldNames;
    };
    
    public int getFieldSize() {
        return this.fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return this.fieldTypes;
    }
    
    public String getPersistentName() {
        return this.getClass().getName();
    }
    
    public String getTableName() {
        return this.TBL_ACC_PAYABLE;
    }
    
    public long deleteExc(Entity ent) throws Exception {
        if(ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return this.deleteExc(ent.getOID());
    }
    
    public long fetchExc(Entity ent) throws Exception {
        AccPayable accPayable = fetchExc(ent.getOID());
        ent = (Entity)accPayable;
        return accPayable.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception {
        return this.updateExc((AccPayable)ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return this.updateExc((AccPayable)ent);
    }
    
    public static AccPayable fetchExc(long oid) throws DBException {
        try {
            AccPayable accPayable = new AccPayable();
            PstAccPayable pstAccPayable = new PstAccPayable(oid);
            accPayable.setOID(oid);
            
            accPayable.setReceiveMaterialId(pstAccPayable.getlong(FLD_RECEIVE_MATERIAL_ID));
            accPayable.setPaymentDate(pstAccPayable.getDate(FLD_PAYMENT_DATE));
            accPayable.setDescription(pstAccPayable.getString(FLD_DESCRIPTION));
            accPayable.setNumOfPayment(pstAccPayable.getInt(FLD_NUM_OF_PAYMENT));
            accPayable.setStatus(pstAccPayable.getInt(FLD_STATUS));
            accPayable.setPaymentNumber(pstAccPayable.getString(FLD_PAYMENT_NUMBER));
            accPayable.setNoUrut(pstAccPayable.getInt(FLD_NO_URUT));
            
            return accPayable;
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstAccPayable(), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(AccPayable accPayable) throws DBException {
        try {
            PstAccPayable pstAccPayable = new PstAccPayable(0);
            
            pstAccPayable.setLong(FLD_RECEIVE_MATERIAL_ID, accPayable.getReceiveMaterialId());
            pstAccPayable.setDate(FLD_PAYMENT_DATE, accPayable.getPaymentDate());
            pstAccPayable.setString(FLD_DESCRIPTION, accPayable.getDescription());
            pstAccPayable.setInt(FLD_NUM_OF_PAYMENT, accPayable.getNumOfPayment());
            pstAccPayable.setInt(FLD_STATUS, accPayable.getStatus());
            pstAccPayable.setString(FLD_PAYMENT_NUMBER, accPayable.getPaymentNumber());
            pstAccPayable.setInt(FLD_NO_URUT, accPayable.getNoUrut());
            
            pstAccPayable.insert();
            accPayable.setOID(pstAccPayable.getlong(FLD_ACC_PAYABLE_ID));
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstAccPayable(0), DBException.UNKNOWN);
        }
        return accPayable.getOID();
    }
    
    public static long updateExc(AccPayable accPayable) throws DBException {
        try {
            if(accPayable != null && accPayable.getOID() != 0) {
                PstAccPayable pstAccPayable = new PstAccPayable(accPayable.getOID());
                
                pstAccPayable.setLong(FLD_RECEIVE_MATERIAL_ID, accPayable.getReceiveMaterialId());
                pstAccPayable.setDate(FLD_PAYMENT_DATE, accPayable.getPaymentDate());
                pstAccPayable.setString(FLD_DESCRIPTION, accPayable.getDescription());
                pstAccPayable.setInt(FLD_NUM_OF_PAYMENT, accPayable.getNumOfPayment());
                pstAccPayable.setInt(FLD_STATUS, accPayable.getStatus());
                pstAccPayable.setString(FLD_PAYMENT_NUMBER, accPayable.getPaymentNumber());
                pstAccPayable.setInt(FLD_NO_URUT, accPayable.getNoUrut());
                pstAccPayable.update();
                return accPayable.getOID();
            }
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstAccPayable(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstAccPayable pstAccPayable = new PstAccPayable(oid);
            pstAccPayable.delete();
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstAccPayable(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static void resultToObject(ResultSet rs, AccPayable accPayable) {
        try {
            
            accPayable.setOID(rs.getLong(PstAccPayable.fieldNames[FLD_ACC_PAYABLE_ID]));
            accPayable.setReceiveMaterialId(rs.getLong(PstAccPayable.fieldNames[FLD_RECEIVE_MATERIAL_ID]));
            accPayable.setPaymentDate(rs.getDate(PstAccPayable.fieldNames[FLD_PAYMENT_DATE]));
            accPayable.setDescription(rs.getString(PstAccPayable.fieldNames[FLD_DESCRIPTION]));
            accPayable.setNumOfPayment(rs.getInt(PstAccPayable.fieldNames[FLD_NUM_OF_PAYMENT]));
            accPayable.setStatus(rs.getInt(PstAccPayable.fieldNames[FLD_STATUS]));
            accPayable.setPaymentNumber(rs.getString(PstAccPayable.fieldNames[FLD_PAYMENT_NUMBER]));
            accPayable.setNoUrut(rs.getInt(PstAccPayable.fieldNames[FLD_NO_URUT]));
            
        } catch(Exception e) {
            System.out.println("resultToObject: "+e.toString());
        }
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_ACC_PAYABLE + " ";
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
                AccPayable accPayable = new AccPayable();
                resultToObject(rs, accPayable);
                lists.add(accPayable);
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
            String sql = "SELECT COUNT(" + PstAccPayable.fieldNames[FLD_ACC_PAYABLE_ID] + ") " +
                    " FROM " + TBL_ACC_PAYABLE;
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
         AccPayable accPayable = PstAccPayable.fetchExc(oid);
         object.put(PstAccPayable.fieldNames[PstAccPayable.FLD_ACC_PAYABLE_ID], accPayable.getOID());
         object.put(PstAccPayable.fieldNames[PstAccPayable.FLD_RECEIVE_MATERIAL_ID], accPayable.getReceiveMaterialId());
         object.put(PstAccPayable.fieldNames[PstAccPayable.FLD_PAYMENT_DATE], accPayable.getPaymentDate());
         object.put(PstAccPayable.fieldNames[PstAccPayable.FLD_DESCRIPTION], accPayable.getDescription());
         object.put(PstAccPayable.fieldNames[PstAccPayable.FLD_NUM_OF_PAYMENT], accPayable.getNumOfPayment());
         object.put(PstAccPayable.fieldNames[PstAccPayable.FLD_STATUS], accPayable.getStatus());
         object.put(PstAccPayable.fieldNames[PstAccPayable.FLD_PAYMENT_NUMBER], accPayable.getPaymentNumber());
         object.put(PstAccPayable.fieldNames[PstAccPayable.FLD_NO_URUT], accPayable.getNoUrut());
      }catch(Exception exc){}
      return object;
   }
}
