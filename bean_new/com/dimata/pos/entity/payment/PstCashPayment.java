/*
 * PstCashPayment.java
 *
 * Created on May 23, 2003, 2:52 PM
 */

package com.dimata.pos.entity.payment;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

import com.dimata.common.entity.payment.*;
/* package java */
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* package qdep */
import java.sql.ResultSet;

/* package cashier */
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.util.Formater;
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.entity.search.SrcSaleReport;
import org.json.JSONObject;

public class PstCashPayment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    //public static final String TBL_PAYMENT = "CASH_PAYMENT";
    public static final String TBL_PAYMENT = "cash_payment";
    
    public static final int FLD_PAYMENT_ID=0;
    public static final int FLD_CURRENCY_ID=1;
    public static final int FLD_BILL_MAIN_ID=2;
    public static final int FLD_PAY_TYPE=3;
    public static final int FLD_RATE=4;
    public static final int FLD_AMOUNT=5;
    public static final int FLD_PAY_DATETIME=6;
    public static final int FLD_PAYMENT_STATUS=7;
    
    public static final String[] fieldNames = {
    "CASH_PAYMENT_ID",
        "CURRENCY_ID",
        "CASH_BILL_MAIN_ID",
        "PAY_TYPE",
        "RATE",
        "AMOUNT",
        "PAY_DATETIME",
        "PAYMENT_STATUS"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG + TYPE_FK,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_INT
    };
    
    //diskon type
    public static final int DISKON_PROSEN=0;
    public static final int DISKON_RUPIAH=1;
    
    
    public static final int UPDATE_STATUS_NONE=0;
    public static final int UPDATE_STATUS_INSERTED=1;
    public static final int UPDATE_STATUS_UPDATED=2;
    public static final int UPDATE_STATUS_DELETED=3;
    
    public static final String[] diskonType = {
        "%",
        "Rp"
    };
    //payment type
    public static final int CASH=0;
    public static final int CARD=1;
    public static final int CHEQUE=2;
    public static final int DEBIT=3;
    public static final int RETURN=4;
    public static final int DP=5;
    public static final int VOUCHER=6;
    public static final int DISCOUNT=7;

    
    public static final String[] paymentType = {
        "CASH",
        "CREDIT CARD",
        "CHEQUE",
        "DEBIT CARD",
        "RETURN",
        "DP",
        "VOUCHER",
        "DISCOUNT"
    };
    /** Creates new PstCashPayment */
    public PstCashPayment() {
    }
    
    public PstCashPayment(int i)throws DBException {
        super(new PstCashPayment());
    }
    
    public PstCashPayment(String sOid) throws DBException {
        super(new PstCashPayment(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstCashPayment(long lOid) throws DBException {
        super(new PstCashPayment(0));
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
        return TBL_PAYMENT;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstCashPayment().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        CashPayments cashPayment = fetchExc(ent.getOID());
        ent = (Entity)cashPayment;
        return cashPayment.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((CashPayments) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((CashPayments) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static CashPayments fetchExc(long oid) throws DBException{
        try{
            CashPayments cashPayment = new CashPayments();
            PstCashPayment pstCashPayment = new PstCashPayment(oid);
            cashPayment.setOID(oid);
            cashPayment.setBillMainId(pstCashPayment.getlong(FLD_BILL_MAIN_ID));
            cashPayment.setCurrencyId(pstCashPayment.getlong(FLD_CURRENCY_ID));
            cashPayment.setPaymentType(pstCashPayment.getInt(FLD_PAY_TYPE));
            cashPayment.setRate(pstCashPayment.getdouble(FLD_RATE));
            cashPayment.setAmount(pstCashPayment.getdouble(FLD_AMOUNT));
            cashPayment.setPayDateTime(pstCashPayment.getDate(FLD_PAY_DATETIME));
            cashPayment.setPaymentStatus(pstCashPayment.getInt(FLD_PAYMENT_STATUS));
            
            return cashPayment;
        }catch(DBException dbe){
            System.out.println(dbe.toString());
            throw dbe;
        }catch(Exception e){
            System.out.println(e.toString());
            throw new DBException(new PstCashPayment(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(CashPayments cashPayment) throws DBException{
        try{
            PstCashPayment pstCashPayment = new PstCashPayment(0);
            pstCashPayment.setLong(FLD_BILL_MAIN_ID,cashPayment.getBillMainId());
            pstCashPayment.setLong(FLD_CURRENCY_ID,cashPayment.getCurrencyId());
            pstCashPayment.setInt(FLD_PAY_TYPE,cashPayment.getPaymentType());
            pstCashPayment.setDouble(FLD_RATE,cashPayment.getRate());
            pstCashPayment.setDouble(FLD_AMOUNT,cashPayment.getAmount());
            pstCashPayment.setDate(FLD_PAY_DATETIME,cashPayment.getPayDateTime());
            pstCashPayment.setInt(FLD_PAYMENT_STATUS,cashPayment.getPaymentStatus());
            
            pstCashPayment.insert();
            cashPayment.setOID(pstCashPayment.getlong(FLD_PAYMENT_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashPayment(0),DBException.UNKNOWN);
        }
        return cashPayment.getOID();
    }

    public static long insertExcByOid(CashPayments cashPayment) throws DBException{
        try{
            PstCashPayment pstCashPayment = new PstCashPayment(0);
            pstCashPayment.setLong(FLD_BILL_MAIN_ID,cashPayment.getBillMainId());
            pstCashPayment.setLong(FLD_CURRENCY_ID,cashPayment.getCurrencyId());
            pstCashPayment.setInt(FLD_PAY_TYPE,cashPayment.getPaymentType());
            pstCashPayment.setDouble(FLD_RATE,cashPayment.getRate());
            pstCashPayment.setDouble(FLD_AMOUNT,cashPayment.getAmount());
            pstCashPayment.setDate(FLD_PAY_DATETIME,cashPayment.getPayDateTime());
            pstCashPayment.setInt(FLD_PAYMENT_STATUS,cashPayment.getPaymentStatus());

            pstCashPayment.insertByOid(cashPayment.getOID());
            //cashPayment.setOID(pstCashPayment.getlong(FLD_PAYMENT_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashPayment(0),DBException.UNKNOWN);
        }
        return cashPayment.getOID();
    }

    public static long updateExc(CashPayments cashPayment) throws DBException{
        try{
            if(cashPayment.getOID() != 0){
                PstCashPayment pstCashPayment = new PstCashPayment(cashPayment.getOID());
                pstCashPayment.setLong(FLD_BILL_MAIN_ID,cashPayment.getBillMainId());
                pstCashPayment.setLong(FLD_CURRENCY_ID,cashPayment.getCurrencyId());
                pstCashPayment.setInt(FLD_PAY_TYPE,cashPayment.getPaymentType());
                pstCashPayment.setDouble(FLD_RATE,cashPayment.getRate());
                pstCashPayment.setDouble(FLD_AMOUNT,cashPayment.getAmount());
                pstCashPayment.setDate(FLD_PAY_DATETIME,cashPayment.getPayDateTime());
                pstCashPayment.setInt(FLD_PAYMENT_STATUS,cashPayment.getPaymentStatus());
                
                pstCashPayment.update();
                return cashPayment.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashPayment(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException{
        try{
            PstCashPayment pstCashPayment = new PstCashPayment(oid);
            pstCashPayment.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashPayment(0),DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_PAYMENT;
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
                CashPayments cashPayment = new CashPayments();
                resultToObject(rs, cashPayment);
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
    
    public static double getPembayaranCash(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT] +
            "* CP." + fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_PAYMENT + " CP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " + fieldNames[FLD_PAY_TYPE] + " != 5" +
            " AND " + fieldNames[FLD_PAY_TYPE] + " !=4" +
            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " =0" +
            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
            " = " + oidCashCashier;

          /*if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;*/

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

    public static double getPembayaranKredit(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT] +
            "* CP." + fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_PAYMENT + " CP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " + fieldNames[FLD_PAY_TYPE] + " != 5" +
            " AND " + fieldNames[FLD_PAY_TYPE] + " !=4" +
            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " =0" +
            //" AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=1" +
            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0" +
            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=1" +
            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] +
            " = " + oidCashCashier;

          /*if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;*/

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

      //List Cash Per Detail Pembayaran
      public static Vector listDetailBayar(int limitStart,int recordToGet, String whereClause){
	Vector lists = new Vector();
	DBResultSet dbrs = null;
	try {
                 String sql = "SELECT " + fieldNames[FLD_PAY_TYPE] +
                         " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                         " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
                         " , CP." + fieldNames[FLD_RATE] +
                         " , CP." + fieldNames[FLD_BILL_MAIN_ID]+
                         " , SUM(CP." + fieldNames[FLD_AMOUNT] + ")" + "AS SUM_AMOUNT" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] +
                         " FROM " + TBL_PAYMENT + " CP" +
                         " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
                         " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
                         " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT" +
                         " ON CP." + fieldNames[FLD_CURRENCY_ID] +
                         " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                         " WHERE " + whereClause +
                         " AND " + fieldNames[FLD_PAY_TYPE] + "!=5" +
                         " AND " + fieldNames[FLD_PAY_TYPE] + "!=4" +
                         " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
                         " GROUP BY " + fieldNames[FLD_PAY_TYPE] +
                         ", CP." + fieldNames[FLD_CURRENCY_ID] +
                         ", CP." + fieldNames[FLD_RATE];

			//if(whereClause != null && whereClause.length() > 0)
				//sql = sql + " WHERE " + whereClause;

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
                Vector temp = new Vector();
                CashPayments cashPayment = new CashPayments();
                CurrencyType currencyType = new CurrencyType();
                BillMain billMain = new BillMain();
                //CashReturn cashReturn = new CashReturn();

                cashPayment.setPaymentType(rs.getInt(1));
                cashPayment.setRate(rs.getInt(4));
                cashPayment.setAmount(rs.getDouble("SUM_AMOUNT" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
                //cashPayment.setAmount(rs.getDouble(6));
                temp.add(cashPayment);

                currencyType.setOID(rs.getLong(2));
                currencyType.setCode(rs.getString(3));
                temp.add(currencyType);

                billMain.setOID(rs.getLong(5));
                temp.add(billMain);
                lists.add(temp);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

      public static Vector listDetailPaymentWithReturn(int limitStart,int recordToGet, String whereClause){
	Vector lists = new Vector();
	DBResultSet dbrs = null;
	try {
                 String sql = "SELECT " + fieldNames[FLD_PAY_TYPE] +
                         " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                         " , CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE] +
                         " , CP." + fieldNames[FLD_RATE] +
                         " , SUM(CP." + fieldNames[FLD_AMOUNT] + ")" +"AS SUM_PAYMENT_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT] +
                         " , SUM(CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+ ")" + "AS SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT] +
                         " FROM " + TBL_PAYMENT + " CP" +
                         " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
                         " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
                         " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT" +
                         " ON CP." + fieldNames[FLD_CURRENCY_ID] +
                         " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
                         " INNER JOIN " + PstCashReturn.TBL_RETURN + " CRP" +
                         " ON CRP." + PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] +
                         " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                         " WHERE " + whereClause +
                        //" AND " + fieldNames[FLD_PAY_TYPE] + "!=5" +
                         //" AND " + fieldNames[FLD_PAY_TYPE] + "!=4" +
                         " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0" +
                         //Add Transaction type and transaction status
                         " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0" +
                         " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0" +
                         //" AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
                         " GROUP BY " + fieldNames[FLD_PAY_TYPE] +
                         ", CP." + fieldNames[FLD_CURRENCY_ID] +
                         ", CP." + fieldNames[FLD_RATE];

			//if(whereClause != null && whereClause.length() > 0)
				//sql = sql + " WHERE " + whereClause;

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
                System.out.println("--->>>" + sql);
                dbrs = DBHandler.execQueryResult(sql);
		ResultSet rs = dbrs.getResultSet();

		while(rs.next()) {
                Vector temp = new Vector();
                CashPayments cashPayment = new CashPayments();
                CurrencyType currencyType = new CurrencyType();
                CashReturn cashReturn = new CashReturn();

                cashPayment.setPaymentType(rs.getInt(1));
                cashPayment.setRate(rs.getInt(4));
                cashPayment.setAmount(rs.getDouble("SUM_PAYMENT_" + PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
                //cashPayment.setAmount(rs.getDouble(5));
                temp.add(cashPayment);

                currencyType.setOID(rs.getLong(2));
                currencyType.setCode(rs.getString(3));
                temp.add(currencyType);


                cashReturn.setAmount(rs.getDouble("SUM_RETURN_" + PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]));
                temp.add(cashReturn);
                lists.add(temp);
                //temp.add(new Double(rs.getDouble("SUM_PRICE") - rs.getDouble("SUM_DISC")));
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
      
    

     
      public static double getSumListDetailBayar(String whereClause) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT] +
            "* CP." + fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_PAYMENT + " CP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " INNER JOIN " + PstCurrencyType.TBL_POS_CURRENCY_TYPE + " CT" +
            " ON CP." + fieldNames[FLD_CURRENCY_ID] +
            " = CT." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] +
            " WHERE " + whereClause +
            " AND " + fieldNames[FLD_PAY_TYPE] + "!=5" +
            " AND " + fieldNames[FLD_PAY_TYPE] + "!=4" +
            " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
            //" GROUP BY " + fieldNames[FLD_PAY_TYPE] +
            //", CP." + fieldNames[FLD_CURRENCY_ID];
            " GROUP BY CP." + fieldNames[FLD_CURRENCY_ID] +
            ", CP." + fieldNames[FLD_RATE];

          /*if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;*/

            System.out.println("--->>>" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }



    public static void resultToObject(ResultSet rs, CashPayments cashPayment){
        try{
            cashPayment.setOID(rs.getLong(PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID]));
            cashPayment.setBillMainId(rs.getLong(PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]));
            cashPayment.setCurrencyId(rs.getLong(PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]));
            cashPayment.setPaymentType(rs.getInt(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]));
            cashPayment.setRate(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]));
            cashPayment.setAmount(rs.getDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]));
            cashPayment.setPaymentStatus(rs.getInt(PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_STATUS]));
            Date dtRoster = DBHandler.convertDate(rs.getDate(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]), rs.getTime(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME]));
            cashPayment.setPayDateTime(dtRoster);
        }catch(Exception e){ }
    }
    
    public static boolean checkOID(long paymentId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PAYMENT +
            " WHERE " + PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID] +
            " = " + paymentId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = true;
            }
            rs.close();
        }
        catch(Exception e) {
            System.out.println("Exc : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static long deleteBillPayments(long oidMain) {
        long oid = 0;
        DBResultSet dbrs = null;
        try {
            String sql = " DELETE FROM " + TBL_PAYMENT+
                    " WHERE " + fieldNames[FLD_BILL_MAIN_ID] + "='" + oidMain + "'";
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return oidMain;
    }
    
    /**
     * overloading method getListPayment(lLocationOid, dStartDate, dEndDate, iDocType, iSaleReportType)
     */
    SrcSaleReport srcSaleReport = new SrcSaleReport();
    public Vector getListPayment(SrcSaleReport srcSaleReport, long lLocationOid, Date dStartDate, Date dEndDate, int iDocType, int iSaleReportType) {
        this.srcSaleReport = srcSaleReport;
        Vector result = getListPayment(lLocationOid, dStartDate,dEndDate, iDocType, iSaleReportType);
        return result;
    }

    /**
     * @param lLocationOid
     * @param dStartDate
     * @param dEndDate
     * @param iSaleReportType
     * @return Vector of (vector of object cashPayment - per payType)
     */
    public Vector getListPayment(long lLocationOid, Date dStartDate, Date dEndDate, int iDocType, int iSaleReportType) {
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
        
        String sStartDate = "";
        String sEndDate = "";
        if(dStartDate!=null && dEndDate!=null) {
            sStartDate = Formater.formatDate(dStartDate,"yyyy-MM-dd") + " 00:00:00";
            sEndDate = Formater.formatDate(dEndDate,"yyyy-MM-dd") + " 23:59:59";
        }
        
        try {
            String sSQLPayment = "SELECT P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+
            ", SUM(P."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+") "+
            " FROM "+PstCashPayment.TBL_PAYMENT+" AS P "+
            " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" AS BM "+
            " ON P."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " WHERE BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
            " BETWEEN \""+sStartDate+ "\""+
            " AND \""+sEndDate+ "\"";
            if(lLocationOid != 0) {
                sSQLPayment = sSQLPayment + " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+
                " = "+lLocationOid;
            }
            sSQLPayment = sSQLPayment + " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+iDocType+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
            " = "+iSaleReportType+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " != "+PstBillMain.TRANS_STATUS_DELETED;
            if(srcSaleReport.getCurrencyOid() != 0) {
                sSQLPayment += " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+" = "+srcSaleReport.getCurrencyOid();
            }
            sSQLPayment += " GROUP BY P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE];
            
            String sSQLChange = "SELECT 0 AS " + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", C." +PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]+
            ", - SUM(C."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+")"+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS M "+
            " INNER JOIN "+PstCashReturn.TBL_RETURN+ " AS C "+
            " ON M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = C."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
            " WHERE M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
            " BETWEEN \""+sStartDate+ "\""+
            " AND \""+sEndDate+ "\"";
            if(lLocationOid != 0) {
                sSQLChange = sSQLChange + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+
                " = "+lLocationOid;
            }
            sSQLChange = sSQLChange + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+iDocType+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
            " = "+iSaleReportType+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " != "+PstBillMain.TRANS_STATUS_DELETED;
            if(srcSaleReport.getCurrencyOid() != 0) {
                sSQLChange += " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+" = "+srcSaleReport.getCurrencyOid();
            }
            sSQLChange += " GROUP BY C."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];
            
            String sql = sSQLPayment + " UNION " + sSQLChange;
            //System.out.println("sql on PstCashPayment.getListPayment(#,#,#,#,#) : " + sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Hashtable hashPayment = new Hashtable();
            while(rs.next()) {
                CashPayments objCashPayments = new CashPayments();
                objCashPayments.setPaymentType(rs.getInt(1));
                objCashPayments.setCurrencyId(rs.getLong(2));
                objCashPayments.setRate(rs.getDouble(3));
                objCashPayments.setAmount(rs.getDouble(4));
                
                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if(objCashPayments.getAmount() != 0) {
                    String strHashKey = ""+objCashPayments.getPaymentType()+objCashPayments.getCurrencyId()+objCashPayments.getRate();
                    if(hashPayment.containsKey(strHashKey)) {
                        CashPayments objCashPaymentsonHash = (CashPayments) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPaymentsonHash.getAmount();
                        double dAdditionalAmount = objCashPayments.getAmount();
                        objCashPaymentsonHash.setAmount(dOriginalAmount+dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPaymentsonHash);
                    }
                    else {
                        hashPayment.put(strHashKey, objCashPayments);
                    }
                }
            }
            
            
            Vector vCashPayment = new Vector(1,1);
            Vector vCardPayment = new Vector(1,1);
            Vector vChequePayment = new Vector(1,1);
            Vector vDebitPayment = new Vector(1,1);
            Vector vReturnPayment = new Vector(1,1);
            for( Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements(); ) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments objCashPaymentResult = (CashPayments) hashPayment.get(strKey);
                int iPayType = Integer.parseInt(strKey.substring(0,1));
                switch(iPayType) {
                    case PstCashPayment.CASH :
                        vCashPayment.add(objCashPaymentResult);
                        break;
                        
                    case PstCashPayment.CARD :
                        vCardPayment.add(objCashPaymentResult);
                        break;
                        
                    case PstCashPayment.CHEQUE :
                        vChequePayment.add(objCashPaymentResult);
                        break;
                        
                    case PstCashPayment.DEBIT :
                        vDebitPayment.add(objCashPaymentResult);
                        break;
                        
                    case PstCashPayment.RETURN :
                        vReturnPayment.add(objCashPaymentResult);
                        break;
                }
            }
            
            result.add(vCashPayment);
            result.add(vCardPayment);
            result.add(vChequePayment);
            result.add(vDebitPayment);
            result.add(vReturnPayment);
        }
        catch(Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString() );
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }


    public Vector getListPayment(SrcSaleReport srcSaleReport,int iDocType, int iSaleReportType) {
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;

        String sStartDate = "";
        String sEndDate = "";
        if(srcSaleReport.getDateFrom()!=null && srcSaleReport.getDateTo()!=null) {
            sStartDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd") + " 00:00:00";
            sEndDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd") + " 23:59:59";
        }

        try {
            String sSQLPayment = "SELECT P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+
            ", SUM(P."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+")"+//*P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+") "+
            "/P." +PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+
            " FROM "+PstCashPayment.TBL_PAYMENT+" AS P "+
            " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" AS BM "+
            " ON P."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " WHERE BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
            " BETWEEN \""+sStartDate+ "\""+
            " AND \""+sEndDate+ "\"";
            if(srcSaleReport.getLocationId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+
                " = "+srcSaleReport.getLocationId();
            }
            if(srcSaleReport.getShiftId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+
                " = "+srcSaleReport.getShiftId();
            }
            sSQLPayment = sSQLPayment + " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+iDocType+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
            " = "+iSaleReportType+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " != "+PstBillMain.TRANS_STATUS_DELETED;
            if(srcSaleReport.getCurrencyOid() != 0) {
                sSQLPayment += " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+" = "+srcSaleReport.getCurrencyOid();
            }
            sSQLPayment += " GROUP BY P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE];

            String sSQLChange = "SELECT 0 AS " + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", C." +PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]+
            ", - SUM(C."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+")"+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS M "+
            " INNER JOIN "+PstCashReturn.TBL_RETURN+ " AS C "+
            " ON M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = C."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
            " WHERE M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
            " BETWEEN \""+sStartDate+ "\""+
            " AND \""+sEndDate+ "\"";
            if(srcSaleReport.getLocationId() != 0) {
                sSQLChange = sSQLChange + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+
                " = "+srcSaleReport.getLocationId();
            }
            if(srcSaleReport.getShiftId() != 0) {
                sSQLChange = sSQLChange + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+
                " = "+srcSaleReport.getShiftId();
            }
            sSQLChange = sSQLChange + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+iDocType+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
            " = "+iSaleReportType+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " != "+PstBillMain.TRANS_STATUS_DELETED;
            if(srcSaleReport.getCurrencyOid() != 0) {
                sSQLChange += " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+" = "+srcSaleReport.getCurrencyOid();
            }
            sSQLChange += " GROUP BY C."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];

            String sql = sSQLPayment + " UNION " + sSQLChange;
            
            //System.out.println("sql on PstCashPayment.getListPayment() : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Hashtable hashPayment = new Hashtable();
            while(rs.next()) {
                CashPayments objCashPayments = new CashPayments();
                objCashPayments.setPaymentType(rs.getInt(1));
                objCashPayments.setCurrencyId(rs.getLong(2));
                objCashPayments.setRate(rs.getDouble(3));
                objCashPayments.setAmount(rs.getDouble(4));

                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if(objCashPayments.getAmount() != 0) {
                    String strHashKey = ""+objCashPayments.getPaymentType()+objCashPayments.getCurrencyId()+objCashPayments.getRate();
                    if(hashPayment.containsKey(strHashKey)) {
                        CashPayments objCashPaymentsonHash = (CashPayments) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPaymentsonHash.getAmount();
                        double dAdditionalAmount = objCashPayments.getAmount();
                        objCashPaymentsonHash.setAmount(dOriginalAmount+dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPaymentsonHash);
                    }
                    else {
                        hashPayment.put(strHashKey, objCashPayments);
                    }
                }
            }


            Vector vCashPayment = new Vector(1,1);
            Vector vCardPayment = new Vector(1,1);
            Vector vChequePayment = new Vector(1,1);
            Vector vDebitPayment = new Vector(1,1);
            Vector vReturnPayment = new Vector(1,1);
            for( Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements(); ) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments objCashPaymentResult = (CashPayments) hashPayment.get(strKey);
                int iPayType = Integer.parseInt(strKey.substring(0,1));
                switch(iPayType) {
                    case PstCashPayment.CASH :
                        vCashPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment.CARD :
                        vCardPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment.CHEQUE :
                        vChequePayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment.DEBIT :
                        vDebitPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment.RETURN :
                        vReturnPayment.add(objCashPaymentResult);
                        break;
                }
            }

            result.add(vCashPayment);
            result.add(vCardPayment);
            result.add(vChequePayment);
            result.add(vDebitPayment);
            result.add(vReturnPayment);
        }
        catch(Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString() );
        }
        finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }


    /**
     * @return Vector of (vector of object cashPayment - per payType)
     */
    public Vector getListPayment(long lCashBillMainOid) {
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
        
        try {
            String sSQLPayment = "SELECT P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+
            ", SUM(P."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+") "+
            " FROM "+PstCashPayment.TBL_PAYMENT+" AS P "+
            " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" AS BM "+
            " ON P."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " WHERE BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = "+lCashBillMainOid +
            " GROUP BY P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE];
            
            String sSQLChange = "SELECT 0 AS " + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", C." +PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]+
            ", - SUM(C."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+")"+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS M "+
            " INNER JOIN "+PstCashReturn.TBL_RETURN+ " AS C "+
            " ON M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = C."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
            " WHERE M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = "+lCashBillMainOid +
            " GROUP BY C."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];
            
            String sql = sSQLPayment + " UNION " + sSQLChange;
            //System.out.println("sql on PstCashPayment.getListPayment() : " + sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Hashtable hashPayment = new Hashtable();
            while(rs.next()) {
                CashPayments objCashPayments = new CashPayments();
                objCashPayments.setPaymentType(rs.getInt(1));
                objCashPayments.setCurrencyId(rs.getLong(2));
                objCashPayments.setRate(rs.getDouble(3));
                objCashPayments.setAmount(rs.getDouble(4));
                
                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if(objCashPayments.getAmount() != 0) {
                    String strHashKey = ""+objCashPayments.getPaymentType()+objCashPayments.getCurrencyId()+objCashPayments.getRate();
                    if(hashPayment.containsKey(strHashKey)) {
                        CashPayments objCashPaymentsonHash = (CashPayments) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPaymentsonHash.getAmount();
                        double dAdditionalAmount = objCashPayments.getAmount();
                        objCashPaymentsonHash.setAmount(dOriginalAmount+dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPaymentsonHash);
                    }
                    else {
                        hashPayment.put(strHashKey, objCashPayments);
                    }
                }
            }
            
            
            Vector vCashPayment = new Vector(1,1);
            Vector vCardPayment = new Vector(1,1);
            Vector vChequePayment = new Vector(1,1);
            Vector vDebitPayment = new Vector(1,1);
            Vector vReturnPayment = new Vector(1,1);
            for( Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements(); ) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments objCashPaymentResult = (CashPayments) hashPayment.get(strKey);
                int iPayType = Integer.parseInt(strKey.substring(0,1));
                switch(iPayType) {
                    case PstCashPayment.CASH :
                        vCashPayment.add(objCashPaymentResult);
                        break;
                        
                    case PstCashPayment.CARD :
                        vCardPayment.add(objCashPaymentResult);
                        break;
                        
                    case PstCashPayment.CHEQUE :
                        vChequePayment.add(objCashPaymentResult);
                        break;
                        
                    case PstCashPayment.DEBIT :
                        vDebitPayment.add(objCashPaymentResult);
                        break;
                        
                    case PstCashPayment.RETURN :
                        vReturnPayment.add(objCashPaymentResult);
                        break;
                }
            }
            
            result.add(vCashPayment);
            result.add(vCardPayment);
            result.add(vChequePayment);
            result.add(vDebitPayment);
            result.add(vReturnPayment);
        }
        catch(Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString() );
        }
        finally {
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
     public static double getPembayaranKotorClosing(long oidCashCashier) {
         return getPembayaranKotorClosing(oidCashCashier,"");
     }
    
    //Pembayaran untuk closing
    public static double getPembayaranKotorClosing(long oidCashCashier, String where) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT] +
            "* CP."+ fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_PAYMENT + " CP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " + " CP." + fieldNames[FLD_PAY_TYPE] + "!= 5" +
            " AND CP." + fieldNames[FLD_PAY_TYPE] + "!=4" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0"+
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0";
            
            if(oidCashCashier==0){
                 sql = sql + " AND "+where;
            }else{
                sql = sql + " AND CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+" = " + oidCashCashier;
            }
            

            //if (whereClause != null && whereClause.length() > 0)
               // sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
}
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }
    
    
    public static double getPembayaranKotorTransactionReturnClosing(long oidCashCashier, String where) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT] +
            "* CP."+ fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_PAYMENT + " CP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " + " CP." + fieldNames[FLD_PAY_TYPE] + "!= 5" +
            " AND CP." + fieldNames[FLD_PAY_TYPE] + "!=4" +
            " AND (CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=1" +        
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0)";
            
            if(oidCashCashier==0){
                 sql = sql + " AND "+where;
            }else{
                sql = sql + " AND CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+" = " + oidCashCashier;
            }
            

            //if (whereClause != null && whereClause.length() > 0)
               // sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
}
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }
    

     public static double getPembayaranKreditClosing(long oidCashCashier) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT] +
            "* CP." + fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_PAYMENT + " CP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " + " CP." + fieldNames[FLD_PAY_TYPE] + "!= 5" +
            " AND CP." + fieldNames[FLD_PAY_TYPE] + "!=4" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0" +
            //" AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=1" +
            " AND CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
            " = " + oidCashCashier;

            //if (whereClause != null && whereClause.length() > 0)
               // sql = sql + " WHERE " + whereClause;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

     //add opie 18-06-2012
    //Pembayaran untuk return
    public static double getPembayaranReturn(long oidCashCashier,int type_payment) {
        double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CP." + fieldNames[FLD_AMOUNT] +
            "* CP."+ fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_PAYMENT + " CP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CP." + fieldNames[FLD_BILL_MAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " + " CP." + fieldNames[FLD_PAY_TYPE] + "=" + type_payment +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=1" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2"  +
            " AND CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
            " = " + oidCashCashier;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(1);
}
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;

    }

     /**
      * getListPaymentDinamis by mirahu 20120416
      * @param srcSaleReport
      * @param iDocType
      * @param iSaleReportType
      * @return 
      */
    public Vector getListPaymentDinamis(SrcSaleReport srcSaleReport,int iDocType, int iSaleReportType) {
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;

        String sStartDate = "";
        String sEndDate = "";
        if(srcSaleReport.getDateFrom()!=null && srcSaleReport.getDateTo()!=null) {
            sStartDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd") + " 00:00:00";
            sEndDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd") + " 23:59:59";
        }

        try {
            String sSQLPayment = "SELECT P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+
            ", SUM(P."+PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT]+")"+//*P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+") "+
            "/P." +PstCashPayment.fieldNames[PstCashPayment.FLD_RATE]+
            " FROM "+PstCashPayment.TBL_PAYMENT+" AS P "+
            " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" AS BM "+
            " ON P."+PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID]+
            " = BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " WHERE BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
            " BETWEEN \""+sStartDate+ "\""+
            " AND \""+sEndDate+ "\"";
            if(srcSaleReport.getLocationId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+
                " = "+srcSaleReport.getLocationId();
            }
            if(srcSaleReport.getShiftId() != 0) {
                sSQLPayment = sSQLPayment + " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+
                " = "+srcSaleReport.getShiftId();
            }
            sSQLPayment = sSQLPayment + " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+iDocType+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
            " = "+iSaleReportType+
            " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " != "+PstBillMain.TRANS_STATUS_DELETED;
            if(srcSaleReport.getCurrencyOid() != 0) {
                sSQLPayment += " AND BM."+PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+" = "+srcSaleReport.getCurrencyOid();
            }
            sSQLPayment += " GROUP BY P."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID]+
            ", P."+PstCashPayment.fieldNames[PstCashPayment.FLD_RATE];

            String sSQLChange = "SELECT 0 AS " + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+
            ", C." +PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]+
            ", - SUM(C."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+")"+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS M "+
            " INNER JOIN "+PstCashReturn.TBL_RETURN+ " AS C "+
            " ON M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = C."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
            " WHERE M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
            " BETWEEN \""+sStartDate+ "\""+
            " AND \""+sEndDate+ "\"";
            if(srcSaleReport.getLocationId() != 0) {
                sSQLChange = sSQLChange + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+
                " = "+srcSaleReport.getLocationId();
            }
            if(srcSaleReport.getShiftId() != 0) {
                sSQLChange = sSQLChange + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+
                " = "+srcSaleReport.getShiftId();
            }
            sSQLChange = sSQLChange + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+iDocType+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
            " = "+iSaleReportType+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+
            " != "+PstBillMain.TRANS_STATUS_DELETED;
            if(srcSaleReport.getCurrencyOid() != 0) {
                sSQLChange += " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+" = "+srcSaleReport.getCurrencyOid();
            }
            sSQLChange += " GROUP BY C."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];

            String sql = sSQLPayment + " UNION " + sSQLChange;
            
            //System.out.println("sql on PstCashPayment.getListPayment() : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Hashtable hashPayment = new Hashtable();
            while(rs.next()) {
                CashPayments objCashPayments = new CashPayments();
                objCashPayments.setPaymentType(rs.getInt(1));
                objCashPayments.setCurrencyId(rs.getLong(2));
                objCashPayments.setRate(rs.getDouble(3));
                objCashPayments.setAmount(rs.getDouble(4));

                // masukkan ke hashtable dengan key adalah kombinasi (paytype+currency+rate)
                if(objCashPayments.getAmount() != 0) {
                    String strHashKey = ""+objCashPayments.getPaymentType()+objCashPayments.getCurrencyId()+objCashPayments.getRate();
                    if(hashPayment.containsKey(strHashKey)) {
                        CashPayments objCashPaymentsonHash = (CashPayments) hashPayment.get(strHashKey);
                        double dOriginalAmount = objCashPaymentsonHash.getAmount();
                        double dAdditionalAmount = objCashPayments.getAmount();
                        objCashPaymentsonHash.setAmount(dOriginalAmount+dAdditionalAmount);
                        hashPayment.put(strHashKey, objCashPaymentsonHash);
                    }
                    else {
                        hashPayment.put(strHashKey, objCashPayments);
                    }
                }
            }


            Vector vCashPayment = new Vector(1,1);
            Vector vCardPayment = new Vector(1,1);
            Vector vChequePayment = new Vector(1,1);
            Vector vDebitPayment = new Vector(1,1);
            Vector vReturnPayment = new Vector(1,1);
            for( Enumeration enumX = hashPayment.keys(); enumX.hasMoreElements(); ) {
                String strKey = String.valueOf(enumX.nextElement());
                CashPayments objCashPaymentResult = (CashPayments) hashPayment.get(strKey);
                //int iPayType = Integer.parseInt(strKey.substring(0,1));
                /*switch(iPayType) {
                    case PstCashPayment.CASH :
                        vCashPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment.CARD :
                        vCardPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment.CHEQUE :
                        vChequePayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment.DEBIT :
                        vDebitPayment.add(objCashPaymentResult);
                        break;

                    case PstCashPayment.RETURN :
                        vReturnPayment.add(objCashPaymentResult);
                        break;
                }*/
                vCashPayment.add(objCashPaymentResult);
            }

            result.add(vCashPayment);
            result.add(vCardPayment);
            result.add(vChequePayment);
            result.add(vDebitPayment);
            result.add(vReturnPayment);
        }
        catch(Exception e) {
            System.out.println("Error on PstCashPayment.getListPayment() : " + e.toString() );
        }
        finally {
            DBResultSet.close(dbrs);

        }
        return result;
    }
	
	public static long insertByOid(CashPayments cashPayments) throws DBException {
      try {
         PstCashPayment pstCashPayment = new PstCashPayment(0);
         pstCashPayment.setLong(FLD_CURRENCY_ID, cashPayments.getCurrencyId());
         pstCashPayment.setLong(FLD_BILL_MAIN_ID, cashPayments.getBillMainId());
         pstCashPayment.setInt(FLD_PAY_TYPE, cashPayments.getPaymentType());
         pstCashPayment.setDouble(FLD_RATE, cashPayments.getRate());
         pstCashPayment.setDouble(FLD_AMOUNT, cashPayments.getAmount());
         pstCashPayment.setDate(FLD_PAY_DATETIME, cashPayments.getPayDateTime());
         pstCashPayment.setInt(FLD_PAYMENT_STATUS, cashPayments.getPaymentStatus());
         pstCashPayment.insertByOid(cashPayments.getOID());
      } catch (DBException dbe) {
         throw dbe;
      } catch (Exception e) {
         throw new DBException(new PstCashPayment(0), DBException.UNKNOWN);
      }
      return cashPayments.getOID();
   }
	
   public static long syncExc(JSONObject jSONObject){
      long oid = 0;
      if (jSONObject != null){
       oid = jSONObject.optLong(PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID],0);
         if (oid > 0){
          CashPayments cashPayments = new CashPayments();
          cashPayments.setOID(jSONObject.optLong(PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_ID],0));
          cashPayments.setCurrencyId(jSONObject.optLong(PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID],0));
          cashPayments.setBillMainId(jSONObject.optLong(PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID],0));
          cashPayments.setPaymentType(jSONObject.optInt(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE],0));
          cashPayments.setRate(jSONObject.optDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_RATE],0));
          cashPayments.setAmount(jSONObject.optDouble(PstCashPayment.fieldNames[PstCashPayment.FLD_AMOUNT],0));
          cashPayments.setPayDateTime(Formater.formatDate(jSONObject.optString(PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME],  "0000-00-00"), "yyyy-MM-dd"));
          cashPayments.setPaymentStatus(jSONObject.optInt(PstCashPayment.fieldNames[PstCashPayment.FLD_PAYMENT_STATUS],0));
         boolean checkOidCashPayments = PstCashPayment.checkOID(oid);
          try{
            if(checkOidCashPayments){
               oid = PstCashPayment.updateExc(cashPayments);
            }else{
               oid = PstCashPayment.insertByOid(cashPayments);
            }
         }catch(Exception exc){
			 oid = 0;
		 }
         }
      }
   return oid;
   }

    

}
