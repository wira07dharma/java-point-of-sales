/*
 * PstCashReturn.java
 *
 * Created on May 23, 2003, 3:53 PM
 */

package com.dimata.pos.entity.payment;

import com.dimata.pos.entity.balance.PstCashCashier;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

/* package java */
import java.util.Vector;

/* package qdep */
import java.sql.ResultSet;
import java.util.Date;
//import com.dimata.qdep.db.*;
/* package cashier */
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Formater;
import com.dimata.util.lang.I_Language;
import org.json.JSONObject;

public class PstCashReturn  extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    //public static final String TBL_RETURN = "CASH_RETURN_PAYMENT";
    public static final String TBL_RETURN = "cash_return_payment";
    
    public static final int FLD_RETURN_ID=0;
    public static final int FLD_BILLMAIN_ID=1;
    public static final int FLD_CURRENCY_ID=2;
    public static final int FLD_RATE=3;
    public static final int FLD_AMOUNT=4;
    public static final int FLD_PAYMENT_STATUS=5;
    
    public static final String[] fieldNames = {
        "RETURN_ID",
        "CASH_BILL_MAIN_ID",
        "CURRENCY_ID",
        "RATE",
        "AMOUNT",
        "PAYMENT_STATUS"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG + TYPE_FK,
        TYPE_LONG + TYPE_FK,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT
    };
    
    
    
    /** Creates new PstCashReturn */
    public PstCashReturn() {
    }
    
    public PstCashReturn(int i)throws DBException {
        super(new PstCashReturn());
    }
    
    public PstCashReturn(String sOid) throws DBException {
        super(new PstCashReturn(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstCashReturn(long lOid) throws DBException {
        super(new PstCashReturn(0));
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
        return TBL_RETURN;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstCashReturn().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        CashReturn cashReturn = fetchExc(ent.getOID());
        ent = (Entity)cashReturn;
        return cashReturn.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((CashReturn) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((CashReturn) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static CashReturn fetchExc(long oid) throws DBException{
        try{
            CashReturn cashReturn = new CashReturn();
            PstCashReturn pstCashReturn = new PstCashReturn(oid);
            cashReturn.setOID(oid);
            cashReturn.setBillMainId(pstCashReturn.getlong(FLD_BILLMAIN_ID));
            cashReturn.setCurrencyId(pstCashReturn.getlong(FLD_CURRENCY_ID));
            cashReturn.setRate(pstCashReturn.getdouble(FLD_RATE));
            cashReturn.setAmount(pstCashReturn.getdouble(FLD_AMOUNT));
            cashReturn.setPaymentStatus(pstCashReturn.getInt(FLD_PAYMENT_STATUS));
            
            return cashReturn;
        }catch(DBException dbe){
            System.out.println(">>>>>>>>"+dbe);
            throw dbe;
        }catch(Exception e){
            System.out.println(">>>>>>>>>>>"+e);
            throw new DBException(new PstCashReturn(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(CashReturn cashReturn) throws DBException{
        try{
            PstCashReturn pstCashReturn = new PstCashReturn(0);
            pstCashReturn.setLong(FLD_BILLMAIN_ID,cashReturn.getBillMainId());
            pstCashReturn.setLong(FLD_CURRENCY_ID,cashReturn.getCurrencyId());
            pstCashReturn.setDouble(FLD_RATE,cashReturn.getRate());
            pstCashReturn.setDouble(FLD_AMOUNT,cashReturn.getAmount());
            pstCashReturn.setInt(FLD_PAYMENT_STATUS,cashReturn.getPaymentStatus());
            pstCashReturn.insert();
            cashReturn.setOID(pstCashReturn.getlong(FLD_RETURN_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashReturn(0),DBException.UNKNOWN);
        }
        return cashReturn.getOID();
    }

    public static long insertExcByOid(CashReturn cashReturn) throws DBException{
        try{
            PstCashReturn pstCashReturn = new PstCashReturn(0);
            pstCashReturn.setLong(FLD_BILLMAIN_ID,cashReturn.getBillMainId());
            pstCashReturn.setLong(FLD_CURRENCY_ID,cashReturn.getCurrencyId());
            pstCashReturn.setDouble(FLD_RATE,cashReturn.getRate());
            pstCashReturn.setDouble(FLD_AMOUNT,cashReturn.getAmount());
            pstCashReturn.setInt(FLD_PAYMENT_STATUS,cashReturn.getPaymentStatus());
            pstCashReturn.insertByOid(cashReturn.getOID());
             //cashReturn.setOID(pstCashReturn.getlong(FLD_RETURN_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashReturn(0),DBException.UNKNOWN);
        }
        return cashReturn.getOID();
    }

    public static long updateExc(CashReturn cashReturn) throws DBException{
        try{
            if(cashReturn.getOID() != 0){
                PstCashReturn pstCashReturn = new PstCashReturn(cashReturn.getOID());
                pstCashReturn.setLong(FLD_BILLMAIN_ID,cashReturn.getBillMainId());
                pstCashReturn.setLong(FLD_CURRENCY_ID,cashReturn.getCurrencyId());
                pstCashReturn.setDouble(FLD_RATE,cashReturn.getRate());
                pstCashReturn.setDouble(FLD_AMOUNT,cashReturn.getAmount());
                pstCashReturn.setInt(FLD_PAYMENT_STATUS,cashReturn.getPaymentStatus());
                pstCashReturn.update();
                return cashReturn.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashReturn(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException{
        try{
            PstCashReturn pstCashReturn = new PstCashReturn(oid);
            pstCashReturn.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCashReturn(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector listAll(){
        return list(0, 500, "","");
    }

    public static long deleteBillReturn(long oidMain) {
        long oid = 0;
        DBResultSet dbrs = null;
        try {
            String sql = " DELETE FROM " + TBL_RETURN+
                    " WHERE " + fieldNames[FLD_BILLMAIN_ID] + "='" + oidMain + "'";
            DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return oidMain;
    }


    public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_RETURN;
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
                CashReturn cashReturn = new CashReturn();
                resultToObject(rs, cashReturn);
                lists.add(cashReturn);
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
    
     public static double getReturnSummary(long oidCashCashier) {
         return getReturnSummary(oidCashCashier,"");
     }
    
     public static double getReturnSummary(long oidCashCashier, String where) {
      double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CRP." + fieldNames[FLD_AMOUNT] +
            "* CRP." + fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_RETURN + " CRP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CRP." + fieldNames[FLD_BILLMAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " +
            " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0";
            //" AND CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
            //" = " + oidCashCashier;
            
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
     
     
     
     public static double getReturnSummaryTransactionReturn(long oidCashCashier, String where) {
      double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CRP." + fieldNames[FLD_AMOUNT] +
            "* CRP." + fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_RETURN + " CRP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CRP." + fieldNames[FLD_BILLMAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " +
            " (CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=1" +
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0"+
            " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=0)";        
            //" AND CBM." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID]+
            //" = " + oidCashCashier;
            
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


     public static double getDetailPaymentReturn(String whereClause) {
      double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(CRP." + fieldNames[FLD_AMOUNT] +
            "* CRP." + fieldNames[FLD_RATE] + ")" +
            " FROM " + TBL_RETURN + " CRP" +
            " INNER JOIN " + PstBillMain.TBL_CASH_BILL_MAIN + " CBM" +
            " ON CRP." + fieldNames[FLD_BILLMAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " INNER JOIN " + PstCashPayment.TBL_PAYMENT + " CP" +
            " ON CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_BILL_MAIN_ID] +
            " = CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
            " WHERE " + whereClause +
            " AND CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE] + "!=5" +
            " AND CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE] + "!=4" +
            " AND CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE] + "!=6" +
            //" AND CP." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=2" +
            " GROUP BY " + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE] +
            ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_CURRENCY_ID] +
            ", CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_RATE];

            //if (whereClause != null && whereClause.length() > 0)
               // sql = sql + " WHERE " + whereClause;

            System.out.println("--->>>SQL CashReturn :" + sql);
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
     
    public static double getReturnByBillMainDate(String whereClause) {
      double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = ""
                + " SELECT"
                + " SUM(crp."+fieldNames[FLD_AMOUNT]+") as "+fieldNames[FLD_AMOUNT]+""
                + " FROM "+TBL_RETURN+" crp"
                + " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" cbm"
                + " ON crp."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = cbm."+fieldNames[FLD_BILLMAIN_ID]+"";

            if (whereClause != null && whereClause.length() > 0)
               sql = sql + " WHERE " + whereClause;

            System.out.println("--->>>SQL CashReturn :" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(fieldNames[FLD_AMOUNT]);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    public static double getReturnByBillMainDateTransactionReturn(String whereClause) {
      double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = ""
                + " SELECT"
                + " SUM(crp."+fieldNames[FLD_AMOUNT]+") as "+fieldNames[FLD_AMOUNT]+""
                + " FROM "+TBL_RETURN+" crp"
                + " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" cbm"
                + " ON crp."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = cbm."+fieldNames[FLD_BILLMAIN_ID]+""
                + " INNER JOIN "+PstCashPayment.TBL_PAYMENT+" cp"
                + " ON cp."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = cbm."+fieldNames[FLD_BILLMAIN_ID]+"" ;

            if (whereClause != null && whereClause.length() > 0)
               sql = sql + " WHERE " + whereClause;

            System.out.println("--->>>SQL CashReturn :" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(fieldNames[FLD_AMOUNT]);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
    
    
    
    public static void resultToObject(ResultSet rs, CashReturn cashReturn){
        try{
            cashReturn.setOID(rs.getLong(PstCashReturn.fieldNames[PstCashReturn.FLD_RETURN_ID]));
            cashReturn.setBillMainId(rs.getLong(PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]));
            cashReturn.setCurrencyId(rs.getLong(PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]));
            cashReturn.setRate(rs.getDouble(PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]));
            cashReturn.setAmount(rs.getDouble(PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]));
            cashReturn.setPaymentStatus(rs.getInt(PstCashReturn.fieldNames[PstCashReturn.FLD_PAYMENT_STATUS]));
        }catch(Exception e){ }
    }
    
    public static boolean checkOID(long returnId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_RETURN +
            " WHERE " + PstCashReturn.fieldNames[PstCashReturn.FLD_RETURN_ID] +
            " = " + returnId;
            
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
    
    
    /**
     * @param lLocationOid
     * @param dStartDate
     * @param dEndDate
     * @param iSaleReportType
     * @return
     */
    public Vector getListChange(long lLocationOid, Date dStartDate, Date dEndDate, int iSaleReportType) {
        Vector result = new Vector(1,1);
        DBResultSet dbrs = null;
        
        String sStartDate = "";
        String sEndDate = "";
        if(dStartDate!=null && dEndDate!=null) {
            sStartDate = Formater.formatDate(dStartDate,"yyyy-MM-dd") + " 00:00:00";
            sEndDate = Formater.formatDate(dEndDate,"yyyy-MM-dd") + " 23:59:59";
        }
        
        try {
            String sql = "SELECT C." +PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE]+
            ", SUM(C."+PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT]+")"+
            " FROM "+PstBillMain.TBL_CASH_BILL_MAIN+ " AS M "+
            " INNER JOIN "+PstCashReturn.TBL_RETURN+ " AS C "+
            " ON M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+
            " = C."+PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID]+
            " WHERE M."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+
            " BETWEEN \""+sStartDate+ "\""+
            " AND \""+sEndDate+ "\"";
            
            if(lLocationOid != 0) {
                sql = sql + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+
                " = "+lLocationOid;
            }
            
            sql = sql + " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+
            " = "+PstBillMain.TYPE_INVOICE+
            " AND M."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+
            " = "+iSaleReportType+
            " GROUP BY C."+PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID]+
            ", C."+PstCashReturn.fieldNames[PstCashReturn.FLD_RATE];
            
            System.out.println("sql on PstCashPayment.getListChange() : " + sql);
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector vCashPayment = new Vector(1,1);
            Vector vCardPayment = new Vector(1,1);
            Vector vChequePayment = new Vector(1,1);
            Vector vDebitPayment = new Vector(1,1);
            while(rs.next()) {
                CashPayments objCashPayments = new CashPayments();
                objCashPayments.setPaymentType(PstCashPayment.CASH);
                objCashPayments.setCurrencyId(rs.getLong(1));
                objCashPayments.setRate(rs.getDouble(2));
                objCashPayments.setAmount(rs.getDouble(3));
                
                vCashPayment.add(objCashPayments);
            }
            
            result.add(vCashPayment);
            result.add(vCardPayment);
            result.add(vChequePayment);
            result.add(vDebitPayment);
        }
        catch(Exception e){
            System.out.println("Error on PstCashPayment.getListChange() : "+e.toString());
        }
        finally{
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println("Test start ...");
        PstCashReturn objPstCashReturn = new PstCashReturn();
        Vector vResult = objPstCashReturn.getListChange(0, new Date(105,4,25), new Date(105,4,25), PstBillMain.TRANS_TYPE_CASH);
        System.out.println("Test finish ...");
    }
    
    
    public static double getReturnByBillMainDateByPaymentSystemType(String whereClause) {
      double count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = ""
                + " SELECT"
                + " SUM(crp."+fieldNames[FLD_AMOUNT]+") as "+fieldNames[FLD_AMOUNT]+""
                + " FROM "+TBL_RETURN+" crp"
                + " INNER JOIN "+PstBillMain.TBL_CASH_BILL_MAIN+" cbm"
                + " ON crp."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+" = cbm."+fieldNames[FLD_BILLMAIN_ID]+""
                + " INNER JOIN cash_payment cp " +
                " ON crp.CASH_BILL_MAIN_ID=cp.CASH_BILL_MAIN_ID " +
                " INNER JOIN payment_system AS ps " +
                " ON ps.PAYMENT_SYSTEM_ID = cp.PAY_TYPE  ";

            if (whereClause != null && whereClause.length() > 0)
               sql = sql + " WHERE " + whereClause;

            System.out.println("--->>>SQL CashReturn :" + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getDouble(fieldNames[FLD_AMOUNT]);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
	
	public static long insertByOid(CashReturn cashReturn) throws DBException {
      try {
         PstCashReturn pstCashReturn = new PstCashReturn(0);
         pstCashReturn.setLong(FLD_RETURN_ID, cashReturn.getOID());
         pstCashReturn.setLong(FLD_BILLMAIN_ID, cashReturn.getBillMainId());
         pstCashReturn.setLong(FLD_CURRENCY_ID, cashReturn.getCurrencyId());
         pstCashReturn.setDouble(FLD_RATE, cashReturn.getRate());
         pstCashReturn.setDouble(FLD_AMOUNT, cashReturn.getAmount());
         pstCashReturn.setInt(FLD_PAYMENT_STATUS, cashReturn.getPaymentStatus());
         pstCashReturn.insertByOid(cashReturn.getOID());
      } catch (DBException dbe) {
         throw dbe;
      } catch (Exception e) {
         throw new DBException(new PstCashReturn(0), DBException.UNKNOWN);
      }
      return cashReturn.getOID();
   }
	
	 public static long syncExc(JSONObject jSONObject){
      long oid = 0;
      if (jSONObject != null){
       oid = jSONObject.optLong(PstCashReturn.fieldNames[PstCashReturn.FLD_RETURN_ID],0);
         if (oid > 0){
          CashReturn cashReturn = new CashReturn();
          cashReturn.setBillMainId(jSONObject.optLong(PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID],0));
          cashReturn.setCurrencyId(jSONObject.optLong(PstCashReturn.fieldNames[PstCashReturn.FLD_CURRENCY_ID],0));
          cashReturn.setRate(jSONObject.optDouble(PstCashReturn.fieldNames[PstCashReturn.FLD_RATE],0));
          cashReturn.setAmount(jSONObject.optDouble(PstCashReturn.fieldNames[PstCashReturn.FLD_AMOUNT],0));
          cashReturn.setPaymentStatus(jSONObject.optInt(PstCashReturn.fieldNames[PstCashReturn.FLD_PAYMENT_STATUS],0));
         boolean checkOidCashReturn = PstCashReturn.checkOID(oid);
          try{
            if(checkOidCashReturn){
               oid = PstCashReturn.updateExc(cashReturn);
            }else{
               oid = PstCashReturn.insertByOid(cashReturn);
            }
         }catch(Exception exc){
			 oid = 0;
		 }
         }
      }
   return oid;
   }
    
}
