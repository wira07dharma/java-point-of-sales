/*
 * PstCashCreditPaymentInfo.java
 *
 * Created on May 23, 2003, 4:18 PM
 */

package com.dimata.pos.entity.payment;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

/* package java */
/* package qdep */
import java.sql.ResultSet;
import java.util.Vector;

//import com.dimata.qdep.db.*;
/* package cashier */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;

public class PstCashCreditPaymentInfo extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    //public static final String TBL_CREDIT_PAYMENT_INFO = "CASH_CREDIT_PAYMENT_INFO";
    public static final String TBL_CREDIT_PAYMENT_INFO = "cash_credit_payment_info";
    
    public static final int FLD_CREDIT_PAYMENT_INFO_ID		= 0;
    public static final int FLD_CASH_CREDIT_PAYMENT_ID	= 1;
    public static final int FLD_CC_NAME		= 2;
    public static final int FLD_CC_NUMBER		= 3;
    public static final int FLD_EXPIRED_DATE	= 4;
    public static final int FLD_HOLDER_NAME	= 5;
    
    public static final int FLD_DEBIT_CARD_NAME   = 6;
    public static final int FLD_DEBIT_BANK_NAME   =7;
    public static final int FLD_CHEQUE_ACCOUNT_NAME = 8;
    public static final int FLD_CHEQUE_DUE_DATE    = 9;
    public static final int FLD_CHEQUE_BANK       =10;
    
    public static final int FLD_CURRENCY_ID = 11;
    public static final int FLD_RATE = 12;
    public static final int FLD_AMOUNT = 13;
    public static final int FLD_TANGGAL_CAIR = 14;
    
    //added by dewok 20180423
    public static final int FLD_BANK_COST = 15;
    
    public static final int UPDATE_STATUS_NONE=0;
    public static final int UPDATE_STATUS_INSERTED=1;
    public static final int UPDATE_STATUS_UPDATED=2;
    public static final int UPDATE_STATUS_DELETED=3;
    
    public static final String[] fieldNames = {
        "CREDIT_PAYMENT_INFO_ID",
        "CASH_CREDIT_PAYMENT_ID",
        "CC_NAME",
        "CC_NUMBER",
        "EXPIRED_DATE",
        "HOLDER_NAME",
        
        "DEBIT_BANK_NAME",
        "DEBIT_CARD_NAME",
        "CHEQUE_ACCOUNT_NAME",
        "CHEQUE_DUE_DATE",
        "CHEQUE_BANK",
        
        "CURRENCY_ID",
        "RATE",
        "AMOUNT",
        "TANGGAL_PENCAIRAN",
        "BANK_COST"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG + TYPE_FK,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_FLOAT
    };
    
    
    /** Creates new PstCashCreditPaymentInfo */
    public PstCashCreditPaymentInfo() {
    }
    
    public PstCashCreditPaymentInfo(int i) throws DBException {
        super(new PstCashCreditPaymentInfo());
    }
    
    public PstCashCreditPaymentInfo(String sOid) throws DBException {
        super(new PstCashCreditPaymentInfo(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstCashCreditPaymentInfo(long lOid) throws DBException {
        super(new PstCashCreditPaymentInfo(0));
        String sOid="0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e) {
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public int getFieldSize(){
        return fieldNames.length;
    }
    
    public String getTableName(){
        return TBL_CREDIT_PAYMENT_INFO;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstCashCreditPaymentInfo().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        CashCreditPaymentInfo cashCreditPaymentInfo = fetchExc(ent.getOID());
        ent = (Entity)cashCreditPaymentInfo;
        return cashCreditPaymentInfo.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((CashCreditPaymentInfo) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((CashCreditPaymentInfo) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    
    public static CashCreditPaymentInfo fetchExc(long oid) throws DBException{
        try{
            CashCreditPaymentInfo cashCreditPaymentInfo = new CashCreditPaymentInfo();
            PstCashCreditPaymentInfo pstCashCreditPaymentInfo = new PstCashCreditPaymentInfo(oid);
            cashCreditPaymentInfo.setOID(oid);
            cashCreditPaymentInfo.setPaymentId(pstCashCreditPaymentInfo.getlong(FLD_CASH_CREDIT_PAYMENT_ID));
            cashCreditPaymentInfo.setCcName(pstCashCreditPaymentInfo.getString(FLD_CC_NAME));
            cashCreditPaymentInfo.setCcNumber(pstCashCreditPaymentInfo.getString(FLD_CC_NUMBER));
            cashCreditPaymentInfo.setExpiredDate(pstCashCreditPaymentInfo.getDate(FLD_EXPIRED_DATE));
            cashCreditPaymentInfo.setHolderName(pstCashCreditPaymentInfo.getString(FLD_HOLDER_NAME));
            
            cashCreditPaymentInfo.setDebitBankName(pstCashCreditPaymentInfo.getString(FLD_DEBIT_BANK_NAME));
            cashCreditPaymentInfo.setDebitCardName(pstCashCreditPaymentInfo.getString(FLD_DEBIT_CARD_NAME));
            cashCreditPaymentInfo.setChequeAccountName(pstCashCreditPaymentInfo.getString(FLD_CHEQUE_ACCOUNT_NAME));
            cashCreditPaymentInfo.setChequeBank(pstCashCreditPaymentInfo.getString(FLD_CHEQUE_BANK));
            cashCreditPaymentInfo.setChequeDueDate(pstCashCreditPaymentInfo.getDate(FLD_CHEQUE_DUE_DATE));
            
            cashCreditPaymentInfo.setCurrencyId(pstCashCreditPaymentInfo.getlong(FLD_CURRENCY_ID));
            cashCreditPaymentInfo.setRate(pstCashCreditPaymentInfo.getdouble(FLD_RATE));
            cashCreditPaymentInfo.setAmount(pstCashCreditPaymentInfo.getdouble(FLD_AMOUNT));
            cashCreditPaymentInfo.setTanggalPencairan(pstCashCreditPaymentInfo.getDate(FLD_TANGGAL_CAIR));
            
            //added by dewok 20180423
            cashCreditPaymentInfo.setBankCost(pstCashCreditPaymentInfo.getdouble(FLD_BANK_COST));
            
            return cashCreditPaymentInfo;
        }catch(DBException dbe){
            System.out.println(">>>>>>>>"+dbe);
            throw dbe;
        }catch(Exception e){
            System.out.println(">>>>>>>>>>>"+e);
            throw new DBException(new PstCashCreditPaymentInfo(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(CashCreditPaymentInfo cashCreditPaymentInfo) throws DBException{
        try{
            PstCashCreditPaymentInfo pstCashCreditPaymentInfo = new PstCashCreditPaymentInfo(0);
            pstCashCreditPaymentInfo.setLong(FLD_CASH_CREDIT_PAYMENT_ID,cashCreditPaymentInfo.getPaymentId());
            pstCashCreditPaymentInfo.setString(FLD_CC_NAME,cashCreditPaymentInfo.getCcName());
            pstCashCreditPaymentInfo.setString(FLD_CC_NUMBER,cashCreditPaymentInfo.getCcNumber());
            pstCashCreditPaymentInfo.setDate(FLD_EXPIRED_DATE,cashCreditPaymentInfo.getExpiredDate());
            pstCashCreditPaymentInfo.setString(FLD_HOLDER_NAME,cashCreditPaymentInfo.getHolderName());
            
            pstCashCreditPaymentInfo.setString(FLD_DEBIT_BANK_NAME,cashCreditPaymentInfo.getDebitBankName());
            pstCashCreditPaymentInfo.setString(FLD_DEBIT_CARD_NAME,cashCreditPaymentInfo.getDebitCardName());
            pstCashCreditPaymentInfo.setString(FLD_CHEQUE_ACCOUNT_NAME,cashCreditPaymentInfo.getChequeAccountName());
            pstCashCreditPaymentInfo.setString(FLD_CHEQUE_BANK,cashCreditPaymentInfo.getChequeBank());
            pstCashCreditPaymentInfo.setDate(FLD_CHEQUE_DUE_DATE,cashCreditPaymentInfo.getChequeDueDate());
            
            pstCashCreditPaymentInfo.setLong(FLD_CURRENCY_ID,cashCreditPaymentInfo.getCurrencyId());
            pstCashCreditPaymentInfo.setDouble(FLD_RATE,cashCreditPaymentInfo.getRate());
            pstCashCreditPaymentInfo.setDouble(FLD_AMOUNT,cashCreditPaymentInfo.getAmount());
            pstCashCreditPaymentInfo.setDate(FLD_TANGGAL_CAIR, cashCreditPaymentInfo.getTanggalPencairan());
            
            //added by dewok 20180423
            pstCashCreditPaymentInfo.setDouble(FLD_BANK_COST, cashCreditPaymentInfo.getBankCost());
            
            pstCashCreditPaymentInfo.insert();
            cashCreditPaymentInfo.setOID(pstCashCreditPaymentInfo.getlong(FLD_CREDIT_PAYMENT_INFO_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashCreditPaymentInfo(0),DBException.UNKNOWN);
        }
        return cashCreditPaymentInfo.getOID();
    }

    public static long insertExcByOid(CashCreditPaymentInfo cashCreditPaymentInfo) throws DBException{
        try{
            PstCashCreditPaymentInfo pstCashCreditPaymentInfo = new PstCashCreditPaymentInfo(0);
            pstCashCreditPaymentInfo.setLong(FLD_CASH_CREDIT_PAYMENT_ID,cashCreditPaymentInfo.getPaymentId());
            pstCashCreditPaymentInfo.setString(FLD_CC_NAME,cashCreditPaymentInfo.getCcName());
            pstCashCreditPaymentInfo.setString(FLD_CC_NUMBER,cashCreditPaymentInfo.getCcNumber());
            pstCashCreditPaymentInfo.setDate(FLD_EXPIRED_DATE,cashCreditPaymentInfo.getExpiredDate());
            pstCashCreditPaymentInfo.setString(FLD_HOLDER_NAME,cashCreditPaymentInfo.getHolderName());

            pstCashCreditPaymentInfo.setString(FLD_DEBIT_BANK_NAME,cashCreditPaymentInfo.getDebitBankName());
            pstCashCreditPaymentInfo.setString(FLD_DEBIT_CARD_NAME,cashCreditPaymentInfo.getDebitCardName());
            pstCashCreditPaymentInfo.setString(FLD_CHEQUE_ACCOUNT_NAME,cashCreditPaymentInfo.getChequeAccountName());
            pstCashCreditPaymentInfo.setString(FLD_CHEQUE_BANK,cashCreditPaymentInfo.getChequeBank());
            pstCashCreditPaymentInfo.setDate(FLD_CHEQUE_DUE_DATE,cashCreditPaymentInfo.getChequeDueDate());

            pstCashCreditPaymentInfo.setLong(FLD_CURRENCY_ID,cashCreditPaymentInfo.getCurrencyId());
            pstCashCreditPaymentInfo.setDouble(FLD_RATE,cashCreditPaymentInfo.getRate());
            pstCashCreditPaymentInfo.setDouble(FLD_AMOUNT,cashCreditPaymentInfo.getAmount());
            pstCashCreditPaymentInfo.setDate(FLD_TANGGAL_CAIR, cashCreditPaymentInfo.getTanggalPencairan());
            
            //added by dewok 20180423
            pstCashCreditPaymentInfo.setDouble(FLD_BANK_COST, cashCreditPaymentInfo.getBankCost());
             
            pstCashCreditPaymentInfo.insertByOid(cashCreditPaymentInfo.getOID());
            //cashCreditPaymentInfo.setOID(pstCashCreditPaymentInfo.getlong(FLD_CREDIT_PAYMENT_INFO_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashCreditPaymentInfo(0),DBException.UNKNOWN);
        }
        return cashCreditPaymentInfo.getOID();
    }

    
    public static long updateExc(CashCreditPaymentInfo cashCreditPaymentInfo) throws DBException{
        try{
            if(cashCreditPaymentInfo.getOID() != 0){
                PstCashCreditPaymentInfo pstCashCreditPaymentInfo = new PstCashCreditPaymentInfo(cashCreditPaymentInfo.getOID());
                pstCashCreditPaymentInfo.setLong(FLD_CASH_CREDIT_PAYMENT_ID,cashCreditPaymentInfo.getPaymentId());
                pstCashCreditPaymentInfo.setString(FLD_CC_NAME,cashCreditPaymentInfo.getCcName());
                pstCashCreditPaymentInfo.setString(FLD_CC_NUMBER,cashCreditPaymentInfo.getCcNumber());
                pstCashCreditPaymentInfo.setDate(FLD_EXPIRED_DATE,cashCreditPaymentInfo.getExpiredDate());
                pstCashCreditPaymentInfo.setString(FLD_HOLDER_NAME,cashCreditPaymentInfo.getHolderName());
                
                pstCashCreditPaymentInfo.setString(FLD_DEBIT_BANK_NAME,cashCreditPaymentInfo.getDebitBankName());
                pstCashCreditPaymentInfo.setString(FLD_DEBIT_CARD_NAME,cashCreditPaymentInfo.getDebitCardName());
                pstCashCreditPaymentInfo.setString(FLD_CHEQUE_ACCOUNT_NAME,cashCreditPaymentInfo.getChequeAccountName());
                pstCashCreditPaymentInfo.setString(FLD_CHEQUE_BANK,cashCreditPaymentInfo.getChequeBank());
                pstCashCreditPaymentInfo.setDate(FLD_CHEQUE_DUE_DATE,cashCreditPaymentInfo.getChequeDueDate());
                
                pstCashCreditPaymentInfo.setLong(FLD_CURRENCY_ID,cashCreditPaymentInfo.getCurrencyId());
                pstCashCreditPaymentInfo.setDouble(FLD_RATE,cashCreditPaymentInfo.getRate());
                pstCashCreditPaymentInfo.setDouble(FLD_AMOUNT,cashCreditPaymentInfo.getAmount());
                
                pstCashCreditPaymentInfo.setDate(FLD_TANGGAL_CAIR, cashCreditPaymentInfo.getTanggalPencairan());
                
                //added by dewok 20180423
                pstCashCreditPaymentInfo.setDouble(FLD_BANK_COST, cashCreditPaymentInfo.getBankCost());
                 
                pstCashCreditPaymentInfo.update();
                return cashCreditPaymentInfo.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashCreditPaymentInfo(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException{
        try{
            PstCashCreditPaymentInfo pstCashCreditPaymentInfo = new PstCashCreditPaymentInfo(oid);
            pstCashCreditPaymentInfo.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashCreditPaymentInfo(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector listAll(){
        return list(0, 500, "","");
    }
    
    public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CREDIT_PAYMENT_INFO;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
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
                    if(limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                CashCreditPaymentInfo cashCreditPaymentInfo = new CashCreditPaymentInfo();
                resultToObject(rs, cashCreditPaymentInfo);
                lists.add(cashCreditPaymentInfo);
            }
            rs.close();
            return lists;
            
        }catch(Exception e) {
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static void resultToObject(ResultSet rs, CashCreditPaymentInfo cashCreditPaymentInfo) {
        try {
            cashCreditPaymentInfo.setOID(rs.getLong(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CREDIT_PAYMENT_INFO_ID]));
            cashCreditPaymentInfo.setPaymentId(rs.getLong(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CASH_CREDIT_PAYMENT_ID]));
            cashCreditPaymentInfo.setCcName(rs.getString(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CC_NAME]));
            cashCreditPaymentInfo.setCcNumber(rs.getString(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CC_NUMBER]));
            cashCreditPaymentInfo.setExpiredDate(rs.getDate(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_EXPIRED_DATE]));
            cashCreditPaymentInfo.setHolderName(rs.getString(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_HOLDER_NAME]));
            
            cashCreditPaymentInfo.setDebitBankName(rs.getString(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_DEBIT_BANK_NAME]));
            cashCreditPaymentInfo.setDebitCardName(rs.getString(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_DEBIT_CARD_NAME]));
            cashCreditPaymentInfo.setChequeAccountName(rs.getString(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_ACCOUNT_NAME]));
            cashCreditPaymentInfo.setChequeDueDate(rs.getDate(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_DUE_DATE]));
            cashCreditPaymentInfo.setChequeBank(rs.getString(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CHEQUE_BANK]));
            
            cashCreditPaymentInfo.setCurrencyId(rs.getLong(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CURRENCY_ID]));
            cashCreditPaymentInfo.setRate(rs.getDouble(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_RATE]));
            cashCreditPaymentInfo.setAmount(rs.getDouble(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_AMOUNT]));
            
            cashCreditPaymentInfo.setTanggalPencairan(rs.getDate(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_TANGGAL_CAIR]));
            
            //added by dewok 20180423
            cashCreditPaymentInfo.setBankCost(rs.getDouble(PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_BANK_COST]));
        }
        catch(Exception e){ }
    }
    
    public static boolean checkOID(long cashCreditPaymentInfoId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CREDIT_PAYMENT_INFO +
            " WHERE " + PstCashCreditPaymentInfo.fieldNames[PstCashCreditPaymentInfo.FLD_CREDIT_PAYMENT_INFO_ID] +
            " = " + cashCreditPaymentInfoId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = true;
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
}
