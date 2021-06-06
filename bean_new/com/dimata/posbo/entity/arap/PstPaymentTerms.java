/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.entity.arap;

import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.Formater;
import com.dimata.posbo.db.*;

import java.util.Vector;
import java.sql.ResultSet;

import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PaymentSystem;
import com.dimata.common.entity.payment.PstPaymentSystem;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import org.json.JSONObject;


/**
 *
 * @author roy ajus
 */
public class PstPaymentTerms extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    public static final String TBL_PAYMENT_TERMS = "payment_terms";
    public static final int FLD_PAYMENT_TERMS_ID = 0;
    public static final int FLD_PURCHASE_ORDER_ID = 1;
    public static final int FLD_RECEIVE_MATERIAL_ID = 2;
    public static final int FLD_DUE_DATE = 3;
    public static final int FLD_PAYMENT_SYSTEM_ID = 4;
    public static final int FLD_CURRENCY_TYPE_ID = 5;
    public static final int FLD_RATE = 6;
    public static final int FLD_AMOUNT = 7;
    public static final int FLD_NOTE = 8;

    public static String fieldNames[] = {
        "PAYMENT_TERMS_ID",
        "PURCHASE_ORDER_ID",
        "RECEIVE_MATERIAL_ID",
        "DUE_DATE",
        "PAYMENT_SYSTEM_ID",
        "CURRENCY_TYPE_ID",
        "RATE",
        "AMOUNT",
        "NOTE"
    };

    public static int fieldTypes[] = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING
    };

    /** Creates a new instance of PstPaymentTerms */
    public PstPaymentTerms() {
    }

    public PstPaymentTerms(int i) throws DBException {
        super(new PstPaymentTerms());
    }

    public PstPaymentTerms(String sOid) throws DBException {
        super(new PstPaymentTerms(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPaymentTerms(long lOid) throws DBException {
        super(new PstPaymentTerms(0));
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
        return this.TBL_PAYMENT_TERMS;
    }

    public long deleteExc(Entity ent) throws Exception {
        if(ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return this.deleteExc(ent.getOID());
    }

    public long fetchExc(Entity ent) throws Exception {
        PaymentTerms paymentTerms = fetchExc(ent.getOID());
        ent = (Entity)paymentTerms;
        return paymentTerms.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return this.updateExc((PaymentTerms)ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return this.updateExc((PaymentTerms)ent);
    }

    public static PaymentTerms fetchExc(long oid) throws DBException {
        try {
            PaymentTerms paymentTerms = new PaymentTerms();
            PstPaymentTerms pstPaymentTerms = new PstPaymentTerms(oid);
            paymentTerms.setOID(oid);

            paymentTerms.setPurchaseOrderId(pstPaymentTerms.getlong(FLD_PURCHASE_ORDER_ID));
            paymentTerms.setReceiveMaterialId(pstPaymentTerms.getlong(FLD_RECEIVE_MATERIAL_ID));
            paymentTerms.setDueDate(pstPaymentTerms.getDate(FLD_DUE_DATE));
            paymentTerms.setPaymentSystemId(pstPaymentTerms.getlong(FLD_PAYMENT_SYSTEM_ID));
            paymentTerms.setCurrencyTypeId(pstPaymentTerms.getlong(FLD_CURRENCY_TYPE_ID));
            paymentTerms.setRate(pstPaymentTerms.getdouble(FLD_RATE));
            paymentTerms.setAmount(pstPaymentTerms.getdouble(FLD_AMOUNT));
            paymentTerms.setNote(pstPaymentTerms.getString(FLD_NOTE));


            return paymentTerms;
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstPaymentTerms(), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PaymentTerms paymentTerms) throws DBException {
        try {
            PstPaymentTerms pstPaymentTerms = new PstPaymentTerms(0);

            pstPaymentTerms.setLong(FLD_PURCHASE_ORDER_ID, paymentTerms.getPurchaseOrderId());
            pstPaymentTerms.setLong(FLD_RECEIVE_MATERIAL_ID, paymentTerms.getReceiveMaterialId());
            pstPaymentTerms.setDate(FLD_DUE_DATE, paymentTerms.getDueDate());
            pstPaymentTerms.setLong(FLD_PAYMENT_SYSTEM_ID, paymentTerms.getPaymentSystemId());
            pstPaymentTerms.setLong(FLD_CURRENCY_TYPE_ID, paymentTerms.getCurrencyTypeId());
            pstPaymentTerms.setDouble(FLD_RATE, paymentTerms.getRate());
            pstPaymentTerms.setDouble(FLD_AMOUNT, paymentTerms.getAmount());

            pstPaymentTerms.insert();
            paymentTerms.setOID(pstPaymentTerms.getlong(FLD_PAYMENT_TERMS_ID));
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstPaymentTerms(0), DBException.UNKNOWN);
        }
        return paymentTerms.getOID();
    }

    public static long updateExc(PaymentTerms paymentTerms) throws DBException {
        try {
            if(paymentTerms != null && paymentTerms.getOID() != 0) {
                PstPaymentTerms pstPaymentTerms = new PstPaymentTerms(paymentTerms.getOID());

                pstPaymentTerms.setLong(FLD_PURCHASE_ORDER_ID, paymentTerms.getPurchaseOrderId());
                pstPaymentTerms.setLong(FLD_RECEIVE_MATERIAL_ID, paymentTerms.getReceiveMaterialId());
                pstPaymentTerms.setDate(FLD_DUE_DATE, paymentTerms.getDueDate());
                pstPaymentTerms.setLong(FLD_PAYMENT_SYSTEM_ID, paymentTerms.getPaymentSystemId());
                pstPaymentTerms.setLong(FLD_CURRENCY_TYPE_ID, paymentTerms.getCurrencyTypeId());
                pstPaymentTerms.setDouble(FLD_RATE, paymentTerms.getRate());
                pstPaymentTerms.setDouble(FLD_AMOUNT, paymentTerms.getAmount());

                pstPaymentTerms.update();
                return paymentTerms.getOID();
            }
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstPaymentTerms(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPaymentTerms pstPaymentTerms = new PstPaymentTerms(oid);
            pstPaymentTerms.delete();
        } catch(DBException dbe) {
            throw dbe;
        } catch(Exception e) {
            throw new DBException(new PstPaymentTerms(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static void resultToObject(ResultSet rs, PaymentTerms paymentTerms) {
        try {
            paymentTerms.setOID(rs.getLong(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_PAYMENT_TERMS_ID]));
            paymentTerms.setPurchaseOrderId(rs.getLong(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_PURCHASE_ORDER_ID]));
            paymentTerms.setReceiveMaterialId(rs.getLong(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_RECEIVE_MATERIAL_ID]));
            paymentTerms.setDueDate(rs.getDate(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_DUE_DATE]));
            paymentTerms.setPaymentSystemId(rs.getLong(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_PAYMENT_SYSTEM_ID]));
            paymentTerms.setCurrencyTypeId(rs.getLong(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_CURRENCY_TYPE_ID]));
            paymentTerms.setRate(rs.getDouble(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_RATE]));
            paymentTerms.setAmount(rs.getDouble(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_AMOUNT]));
            paymentTerms.setNote(rs.getString(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_NOTE]));
        } catch(Exception e) {
            System.out.println("resultToObject: "+e.toString());
        }
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PAYMENT_TERMS + " ";
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
                PaymentTerms paymentTerms = new PaymentTerms();
                resultToObject(rs, paymentTerms);
                lists.add(paymentTerms);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

     //Untuk menampilkan list payment terms di receive material
    public static Vector listPaymentTerms(int limitStart, int recordToGet, String whereClause, String order) {
          if(limitStart<0){
             limitStart=0;
        }
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
              String sql = "SELECT * FROM " + TBL_PAYMENT_TERMS + " TERMS"
              //String sql = "SELECT TERMS." + fieldNames[FLD_DUE_DATE]
                   // + " , PAY." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]
                   // + " , PAY." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM]
                   // + " , CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                   // + " , CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
                   // + " , TERMS." + fieldNames[FLD_RATE]
                   // + " , TERMS." + fieldNames[FLD_AMOUNT]
                   // + " , TERMS." + fieldNames[FLD_NOTE]
                   // + " , TERMS." + fieldNames[FLD_RECEIVE_MATERIAL_ID]
                    //+ " FROM " + TBL_PAYMENT_TERMS + " TERMS"
                    + " LEFT JOIN " + PstPaymentSystem.TBL_P2_PAYMENT_SYSTEM + " PAY"
                    + " ON TERMS." + fieldNames[FLD_PAYMENT_SYSTEM_ID]
                    + " = PAY." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID]
                    + " LEFT JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CURR"
                    + " ON TERMS." + fieldNames[FLD_CURRENCY_TYPE_ID]
                    + " = CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " INNER JOIN " + PstMatReceive.TBL_MAT_RECEIVE + " REC"
                    + " ON TERMS." + fieldNames[FLD_RECEIVE_MATERIAL_ID]
                    + " = REC." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];

            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
              
            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                /*Vector temp = new Vector();
                PaymentTerms paymentTerms = new PaymentTerms();
                PaymentSystem paymentSystem = new PaymentSystem();
                CurrencyType currencyType = new CurrencyType();

                paymentTerms.setDueDate(rs.getDate(0));
                paymentTerms.setRate(rs.getDouble(5));
                paymentTerms.setAmount(rs.getDouble(6));
                paymentTerms.setNote(rs.getString(7));
                temp.add(paymentTerms);

                paymentSystem.setOID(rs.getLong(1));
                paymentSystem.setPaymentSystem(rs.getString(2));
                temp.add(paymentSystem);

                currencyType.setOID(rs.getLong(3));
                currencyType.setCode(rs.getString(4));
                temp.add(currencyType); */

             
                //lists.add(temp);
                PaymentTerms paymentTerms = new PaymentTerms();
                resultToObject(rs, paymentTerms);
                lists.add(paymentTerms);
                
            }
            rs.close();

        } catch (Exception e) {
            //lists = new Vector();
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(TERMS." + PstPaymentTerms.fieldNames[FLD_PAYMENT_TERMS_ID] + ") " +
                    " FROM " + TBL_PAYMENT_TERMS+ " TERMS";
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
         PaymentTerms paymentTerms = PstPaymentTerms.fetchExc(oid);
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_PAYMENT_TERMS_ID], paymentTerms.getOID());
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_PURCHASE_ORDER_ID], paymentTerms.getPurchaseOrderId());
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_RECEIVE_MATERIAL_ID], paymentTerms.getReceiveMaterialId());
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_DUE_DATE], paymentTerms.getDueDate());
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_PAYMENT_SYSTEM_ID], paymentTerms.getPaymentSystemId());
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_CURRENCY_TYPE_ID], paymentTerms.getCurrencyTypeId());
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_RATE], paymentTerms.getRate());
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_AMOUNT], paymentTerms.getAmount());
         object.put(PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_NOTE], paymentTerms.getNote());
      }catch(Exception exc){}
      return object;
   }

}


