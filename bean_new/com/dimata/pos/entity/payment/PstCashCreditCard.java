/*
 * PstCashCreditCard.java
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
import com.dimata.util.Formater;
import com.dimata.util.lang.I_Language;
import org.json.JSONObject;

public class PstCashCreditCard extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    //public static final String TBL_CREDIT_CARD = "CASH_CREDIT_CARD";
    public static final String TBL_CREDIT_CARD = "cash_credit_card";
    
    public static final int FLD_CC_ID		= 0;
    public static final int FLD_PAYMENT_ID	= 1;
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
    
    public static final int UPDATE_STATUS_NONE=0;
    public static final int UPDATE_STATUS_INSERTED=1;
    public static final int UPDATE_STATUS_UPDATED=2;
    public static final int UPDATE_STATUS_DELETED=3;
    /**
     *    private String debitBankName = "";
    private String debitCardName = "";
    private String chequeAccountName = "";
    private Date chequeDueDate = new Date();
    private String chequeBank = "";
     *
     */
    
    public static final String[] fieldNames = {
        "CC_ID",
        "CASH_PAYMENT_ID",
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
        "AMOUNT"
        
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG + TYPE_PK,
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
        TYPE_FLOAT
    };
    
    
    /** Creates new PstCashCreditCard */
    public PstCashCreditCard() {
    }
    
    public PstCashCreditCard(int i)throws DBException {
        super(new PstCashCreditCard());
    }
    
    public PstCashCreditCard(String sOid) throws DBException {
        super(new PstCashCreditCard(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstCashCreditCard(long lOid) throws DBException {
        super(new PstCashCreditCard(0));
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
        return TBL_CREDIT_CARD;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstCashCreditCard().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        CashCreditCard cashCreditCard = fetchExc(ent.getOID());
        ent = (Entity)cashCreditCard;
        return cashCreditCard.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((CashCreditCard) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((CashCreditCard) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    
    public static CashCreditCard fetchExc(long oid) throws DBException{
        try{
            CashCreditCard cashCreditCard = new CashCreditCard();
            PstCashCreditCard pstCashCreditCard = new PstCashCreditCard(oid);
            cashCreditCard.setOID(oid);
            cashCreditCard.setPaymentId(pstCashCreditCard.getlong(FLD_PAYMENT_ID));
            cashCreditCard.setCcName(pstCashCreditCard.getString(FLD_CC_NAME));
            cashCreditCard.setCcNumber(pstCashCreditCard.getString(FLD_CC_NUMBER));
            cashCreditCard.setExpiredDate(pstCashCreditCard.getDate(FLD_EXPIRED_DATE));
            cashCreditCard.setHolderName(pstCashCreditCard.getString(FLD_HOLDER_NAME));
            
            cashCreditCard.setDebitBankName(pstCashCreditCard.getString(FLD_DEBIT_BANK_NAME));
            cashCreditCard.setDebitCardName(pstCashCreditCard.getString(FLD_DEBIT_CARD_NAME));
            cashCreditCard.setChequeAccountName(pstCashCreditCard.getString(FLD_CHEQUE_ACCOUNT_NAME));
            cashCreditCard.setChequeBank(pstCashCreditCard.getString(FLD_CHEQUE_BANK));
            cashCreditCard.setChequeDueDate(pstCashCreditCard.getDate(FLD_CHEQUE_DUE_DATE));
            
            cashCreditCard.setCurrencyId(pstCashCreditCard.getlong(FLD_CURRENCY_ID));
            cashCreditCard.setRate(pstCashCreditCard.getdouble(FLD_RATE));
            cashCreditCard.setAmount(pstCashCreditCard.getdouble(FLD_AMOUNT));
            
            return cashCreditCard;
        }catch(DBException dbe){
            System.out.println(">>>>>>>>"+dbe);
            throw dbe;
        }catch(Exception e){
            System.out.println(">>>>>>>>>>>"+e);
            throw new DBException(new PstCashCreditCard(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(CashCreditCard cashCreditCard) throws DBException{
        try{
            PstCashCreditCard pstCashCreditCard = new PstCashCreditCard(0);
            pstCashCreditCard.setLong(FLD_PAYMENT_ID,cashCreditCard.getPaymentId());
            pstCashCreditCard.setString(FLD_CC_NAME,cashCreditCard.getCcName());
            pstCashCreditCard.setString(FLD_CC_NUMBER,cashCreditCard.getCcNumber());
            pstCashCreditCard.setDate(FLD_EXPIRED_DATE,cashCreditCard.getExpiredDate());
            pstCashCreditCard.setString(FLD_HOLDER_NAME,cashCreditCard.getHolderName());
            
            pstCashCreditCard.setString(FLD_DEBIT_BANK_NAME,cashCreditCard.getDebitBankName());
            pstCashCreditCard.setString(FLD_DEBIT_CARD_NAME,cashCreditCard.getDebitCardName());
            pstCashCreditCard.setString(FLD_CHEQUE_ACCOUNT_NAME,cashCreditCard.getChequeAccountName());
            pstCashCreditCard.setString(FLD_CHEQUE_BANK,cashCreditCard.getChequeBank());
            pstCashCreditCard.setDate(FLD_CHEQUE_DUE_DATE,cashCreditCard.getChequeDueDate());
            
            pstCashCreditCard.setLong(FLD_CURRENCY_ID,cashCreditCard.getCurrencyId());
            pstCashCreditCard.setDouble(FLD_RATE,cashCreditCard.getRate());
            pstCashCreditCard.setDouble(FLD_AMOUNT,cashCreditCard.getAmount());
            
            pstCashCreditCard.insert();
            cashCreditCard.setOID(pstCashCreditCard.getlong(FLD_CC_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashCreditCard(0),DBException.UNKNOWN);
        }
        return cashCreditCard.getOID();
    }

    public static long insertExcByOid(CashCreditCard cashCreditCard) throws DBException{
        try{
            PstCashCreditCard pstCashCreditCard = new PstCashCreditCard(0);
            pstCashCreditCard.setLong(FLD_PAYMENT_ID,cashCreditCard.getPaymentId());
            pstCashCreditCard.setString(FLD_CC_NAME,cashCreditCard.getCcName());
            pstCashCreditCard.setString(FLD_CC_NUMBER,cashCreditCard.getCcNumber());
            pstCashCreditCard.setDate(FLD_EXPIRED_DATE,cashCreditCard.getExpiredDate());
            pstCashCreditCard.setString(FLD_HOLDER_NAME,cashCreditCard.getHolderName());

            pstCashCreditCard.setString(FLD_DEBIT_BANK_NAME,cashCreditCard.getDebitBankName());
            pstCashCreditCard.setString(FLD_DEBIT_CARD_NAME,cashCreditCard.getDebitCardName());
            pstCashCreditCard.setString(FLD_CHEQUE_ACCOUNT_NAME,cashCreditCard.getChequeAccountName());
            pstCashCreditCard.setString(FLD_CHEQUE_BANK,cashCreditCard.getChequeBank());
            pstCashCreditCard.setDate(FLD_CHEQUE_DUE_DATE,cashCreditCard.getChequeDueDate());

            pstCashCreditCard.setLong(FLD_CURRENCY_ID,cashCreditCard.getCurrencyId());
            pstCashCreditCard.setDouble(FLD_RATE,cashCreditCard.getRate());
            pstCashCreditCard.setDouble(FLD_AMOUNT,cashCreditCard.getAmount());

            pstCashCreditCard.insertByOid(TYPE_ID);
            //cashCreditCard.setOID(pstCashCreditCard.getlong(FLD_CC_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashCreditCard(0),DBException.UNKNOWN);
        }
        return cashCreditCard.getOID();
    }


    public static long updateExc(CashCreditCard cashCreditCard) throws DBException{
        try{
            if(cashCreditCard.getOID() != 0){
                PstCashCreditCard pstCashCreditCard = new PstCashCreditCard(cashCreditCard.getOID());
                pstCashCreditCard.setLong(FLD_PAYMENT_ID,cashCreditCard.getPaymentId());
                pstCashCreditCard.setString(FLD_CC_NAME,cashCreditCard.getCcName());
                pstCashCreditCard.setString(FLD_CC_NUMBER,cashCreditCard.getCcNumber());
                pstCashCreditCard.setDate(FLD_EXPIRED_DATE,cashCreditCard.getExpiredDate());
                pstCashCreditCard.setString(FLD_HOLDER_NAME,cashCreditCard.getHolderName());
                
                pstCashCreditCard.setString(FLD_DEBIT_BANK_NAME,cashCreditCard.getDebitBankName());
                pstCashCreditCard.setString(FLD_DEBIT_CARD_NAME,cashCreditCard.getDebitCardName());
                pstCashCreditCard.setString(FLD_CHEQUE_ACCOUNT_NAME,cashCreditCard.getChequeAccountName());
                pstCashCreditCard.setString(FLD_CHEQUE_BANK,cashCreditCard.getChequeBank());
                pstCashCreditCard.setDate(FLD_CHEQUE_DUE_DATE,cashCreditCard.getChequeDueDate());
                
                pstCashCreditCard.setLong(FLD_CURRENCY_ID,cashCreditCard.getCurrencyId());
                pstCashCreditCard.setDouble(FLD_RATE,cashCreditCard.getRate());
                pstCashCreditCard.setDouble(FLD_AMOUNT,cashCreditCard.getAmount());
                
                pstCashCreditCard.update();
                return cashCreditCard.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashCreditCard(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException{
        try{
            PstCashCreditCard pstCashCreditCard = new PstCashCreditCard(oid);
            pstCashCreditCard.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashCreditCard(0),DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_CREDIT_CARD;
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
                CashCreditCard cashCreditCard = new CashCreditCard();
                resultToObject(rs, cashCreditCard);
                lists.add(cashCreditCard);
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
    
    public static void resultToObject(ResultSet rs, CashCreditCard cashCreditCard) {
        try {
            cashCreditCard.setOID(rs.getLong(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_ID]));
            cashCreditCard.setPaymentId(rs.getLong(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID]));
            cashCreditCard.setCcName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NAME]));
            cashCreditCard.setCcNumber(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NUMBER]));
            cashCreditCard.setExpiredDate(rs.getDate(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_EXPIRED_DATE]));
            cashCreditCard.setHolderName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_HOLDER_NAME]));
            
            cashCreditCard.setDebitBankName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_BANK_NAME]));
            cashCreditCard.setDebitCardName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_CARD_NAME]));
            cashCreditCard.setChequeAccountName(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_ACCOUNT_NAME]));
            cashCreditCard.setChequeDueDate(rs.getDate(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_DUE_DATE]));
            cashCreditCard.setChequeBank(rs.getString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_BANK]));
            
            cashCreditCard.setCurrencyId(rs.getLong(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CURRENCY_ID]));
            cashCreditCard.setRate(rs.getDouble(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_RATE]));
            cashCreditCard.setAmount(rs.getDouble(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_AMOUNT]));
            
        }
        catch(Exception e){ }
    }
    
    public static boolean checkOID(long cashCreditCardId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CREDIT_CARD +
            " WHERE " + PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_ID] +
            " = " + cashCreditCardId;
            
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
    
    
    public static Vector listDetailPaymentCard(String where){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
           String sql = " SELECT cash_payment.AMOUNT AS CREDIT_CARD, ps.PAYMENT_SYSTEM AS TYPE_OF_CARD, " +
                        " cash_credit_card.CC_NUMBER AS CARD_NUMBER, cash_credit_card.CC_NAME AS NAME_ON_CARD, cash_credit_card.DEBIT_BANK_NAME AS BANK_NAME, " +
                        " cash_credit_card.EXPIRED_DATE" +
                        " FROM cash_payment " +
                        " INNER JOIN cash_credit_card ON cash_payment.CASH_PAYMENT_ID=cash_credit_card.CASH_PAYMENT_ID " +
                        " INNER JOIN payment_system AS ps ON ps.PAYMENT_SYSTEM_ID=cash_payment.PAY_TYPE";
            sql = sql +" WHERE "+where;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                CashCreditCard cashPayment = new CashCreditCard();
                cashPayment.setAmount(rs.getDouble("CREDIT_CARD"));
                cashPayment.setTypeOfCard(rs.getString("TYPE_OF_CARD"));
                cashPayment.setCcName(rs.getString("NAME_ON_CARD"));
                cashPayment.setCcNumber(rs.getString("CARD_NUMBER"));
                cashPayment.setDebitBankName(rs.getString("BANK_NAME"));
                cashPayment.setExpiredDate(rs.getDate("EXPIRED_DATE"));
                lists.add(cashPayment);
            }
            rs.close();
            return lists;
            
        }catch(Exception e) {
            System.out.println(e.toString());
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
	
	 public static long insertByOid(CashCreditCard cashCreditCard) throws DBException {
      try {
         PstCashCreditCard pstCashCreditCard = new PstCashCreditCard(0);
         pstCashCreditCard.setLong(FLD_PAYMENT_ID, cashCreditCard.getPaymentId());
         pstCashCreditCard.setString(FLD_CC_NAME, cashCreditCard.getCcName());
         pstCashCreditCard.setString(FLD_CC_NUMBER, cashCreditCard.getCcNumber());
         pstCashCreditCard.setDate(FLD_EXPIRED_DATE, cashCreditCard.getExpiredDate());
         pstCashCreditCard.setString(FLD_HOLDER_NAME, cashCreditCard.getHolderName());
         pstCashCreditCard.setString(FLD_DEBIT_CARD_NAME, cashCreditCard.getDebitBankName());
         pstCashCreditCard.setString(FLD_DEBIT_BANK_NAME, cashCreditCard.getDebitCardName());
         pstCashCreditCard.setString(FLD_CHEQUE_ACCOUNT_NAME, cashCreditCard.getChequeAccountName());
         pstCashCreditCard.setDate(FLD_CHEQUE_DUE_DATE, cashCreditCard.getChequeDueDate());
         pstCashCreditCard.setString(FLD_CHEQUE_BANK, cashCreditCard.getChequeBank());
         pstCashCreditCard.setLong(FLD_CURRENCY_ID, cashCreditCard.getCurrencyId());
         pstCashCreditCard.setDouble(FLD_RATE, cashCreditCard.getRate());
         pstCashCreditCard.setDouble(FLD_AMOUNT, cashCreditCard.getAmount());
         pstCashCreditCard.insertByOid(cashCreditCard.getOID());
      } catch (DBException dbe) {
         throw dbe;
      } catch (Exception e) {
         throw new DBException(new PstCashCreditCard(0), DBException.UNKNOWN);
      }
      return cashCreditCard.getOID();
   }
	
	public static long syncExc(JSONObject jSONObject){
      long oid = 0;
      if (jSONObject != null){
       oid = jSONObject.optLong(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_ID],0);
         if (oid > 0){
          CashCreditCard cashCreditCard = new CashCreditCard();
          cashCreditCard.setOID(jSONObject.optLong(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_ID],0));
          cashCreditCard.setPaymentId(jSONObject.optLong(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_PAYMENT_ID],0));
          cashCreditCard.setCcName(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NAME], ""));
          cashCreditCard.setCcNumber(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CC_NUMBER], ""));
          cashCreditCard.setExpiredDate(Formater.formatDate(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_EXPIRED_DATE],  "0000-00-00"), "yyyy-MM-dd"));
          cashCreditCard.setHolderName(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_HOLDER_NAME], ""));
          cashCreditCard.setDebitBankName(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_CARD_NAME], ""));
          cashCreditCard.setDebitCardName(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_DEBIT_BANK_NAME], ""));
          cashCreditCard.setChequeAccountName(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_ACCOUNT_NAME], ""));
          cashCreditCard.setChequeDueDate(Formater.formatDate(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_DUE_DATE],  "0000-00-00"), "yyyy-MM-dd"));
          cashCreditCard.setChequeBank(jSONObject.optString(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CHEQUE_BANK], ""));
          cashCreditCard.setCurrencyId(jSONObject.optLong(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_CURRENCY_ID],0));
          cashCreditCard.setRate(jSONObject.optDouble(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_RATE],0));
          cashCreditCard.setAmount(jSONObject.optDouble(PstCashCreditCard.fieldNames[PstCashCreditCard.FLD_AMOUNT],0));
         boolean checkOidCashCreditCard = PstCashCreditCard.checkOID(oid);
          try{
            if(checkOidCashCreditCard){
               oid = PstCashCreditCard.updateExc(cashCreditCard);
            }else{
               oid = PstCashCreditCard.insertByOid(cashCreditCard);
            }
         }catch(Exception exc){
			 oid = 0;
		 }
         }
      }
   return oid;
   }
    
}
