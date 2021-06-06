/*
 * PstPaymentTerm.java
 *
 * Created on November 12, 2007, 2:25 PM
 */

package com.dimata.aiso.entity.invoice;

/**
 *
 * @author  dwi
 */
import java.sql.ResultSet;
import java.util.*;

import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.interfaces.journal.I_JournalType;

public class PstPaymentTerm extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_JournalType{
    
    public static final String TBL_PAY_TERM = "aiso_pay_term";
    
    public static int FLD_PAY_TERM_ID = 0;
    public static int FLD_PLAN_DATE = 1;
    public static int FLD_AMOUNT = 2;
    public static int FLD_NOTE = 3;
    public static int FLD_TYPE = 4;
    public static int FLD_INVOICE_ID = 5;
    
    public static String[] fieldNames = {
        "PAY_TERM_ID",
        "PLAN_DATE",
        "AMOUNT",
        "NOTE",
        "TYPE",
        "INVOICE_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_LONG
    };
    
    public static final int PAY_TERM_AR = 0;
    public static final int PAY_TERM_AP = 1;
    
    /** Creates a new instance of PstPaymentTerm */
    public PstPaymentTerm() {
    }
    
    public PstPaymentTerm(int i) throws DBException {
        super(new PstPaymentTerm());
    }
    
     public PstPaymentTerm(String sOid) throws DBException {
        super(new PstPaymentTerm(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
     
     public PstPaymentTerm(long lOid) throws DBException {
        super(new PstPaymentTerm(0));
        String sOid = "0";
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
    
    public long deleteExc(Entity entity) throws Exception {
        return deleteExc((PaymentTerm)entity);
    }
    
    public long fetchExc(Entity entity) throws Exception {
        PaymentTerm objPaymentTerm = PstPaymentTerm.fetchExc(entity.getOID());
        entity = (Entity)objPaymentTerm;
        return objPaymentTerm.getOID();
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
        return new PstPaymentTerm().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_PAY_TERM;
    }
    
    public long insertExc(Entity entity) throws Exception {
        return PstPaymentTerm.insertExc((PaymentTerm)entity);
    }
    
    public long updateExc(Entity entity) throws Exception {
        return PstPaymentTerm.updateExc((PaymentTerm)entity);
    }
    
    public static PaymentTerm fetchExc(long lOid) throws DBException{
        try{
            PaymentTerm objPaymentTerm = new PaymentTerm();
            PstPaymentTerm pstPaymentTerm = new PstPaymentTerm(lOid);
            objPaymentTerm.setOID(lOid);
            
            objPaymentTerm.setAmount(pstPaymentTerm.getdouble(FLD_AMOUNT));
            objPaymentTerm.setNote(pstPaymentTerm.getString(FLD_NOTE));
            objPaymentTerm.setPlanDate(pstPaymentTerm.getDate(FLD_PLAN_DATE));
            objPaymentTerm.setType(pstPaymentTerm.getInt(FLD_TYPE));
            objPaymentTerm.setInvoiceId(pstPaymentTerm.getlong(FLD_INVOICE_ID));
            
            return objPaymentTerm;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstPaymentTerm(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(PaymentTerm objPaymentTerm) throws DBException{
        try{
            PstPaymentTerm pstPaymentTerm = new PstPaymentTerm(0);
            
            pstPaymentTerm.setDouble(FLD_AMOUNT, objPaymentTerm.getAmount());
            pstPaymentTerm.setString(FLD_NOTE, objPaymentTerm.getNote());
            pstPaymentTerm.setDate(FLD_PLAN_DATE, objPaymentTerm.getPlanDate());
            pstPaymentTerm.setInt(FLD_TYPE, objPaymentTerm.getType());
            pstPaymentTerm.setLong(FLD_INVOICE_ID, objPaymentTerm.getInvoiceId());
            
            pstPaymentTerm.insert();
            objPaymentTerm.setOID(pstPaymentTerm.getlong(FLD_PAY_TERM_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstPaymentTerm(0), DBException.UNKNOWN);
        }
        return objPaymentTerm.getOID();
    }
    
    public static long updateExc(PaymentTerm objPaymentTerm) throws DBException{
        try{
            if(objPaymentTerm.getOID() != 0){
                PstPaymentTerm pstPaymentTerm = new PstPaymentTerm(objPaymentTerm.getOID());
                
                pstPaymentTerm.setDouble(FLD_AMOUNT, objPaymentTerm.getAmount());
                pstPaymentTerm.setString(FLD_NOTE, objPaymentTerm.getNote());
                pstPaymentTerm.setDate(FLD_PLAN_DATE, objPaymentTerm.getPlanDate());
                pstPaymentTerm.setInt(FLD_TYPE, objPaymentTerm.getType());
                pstPaymentTerm.setLong(FLD_INVOICE_ID, objPaymentTerm.getInvoiceId());

                pstPaymentTerm.update();            
                return objPaymentTerm.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstPaymentTerm(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstPaymentTerm pstPaymentTerm = new PstPaymentTerm(lOid);
            pstPaymentTerm.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstPaymentTerm(0), DBException.UNKNOWN);
        }
        return lOid;
    }
    
    public static Vector list(int iStart, int iRecordToGet, String strWhClause, String strOrderBy){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
            try{
                String sql = " SELECT * FROM "+TBL_PAY_TERM;
                if(strWhClause != null && strWhClause.length() > 0)
                    sql += " WHERE "+strWhClause;
                if(strOrderBy != null && strOrderBy.length() > 0)
                    sql += " ORDER BY "+strOrderBy;
                switch(DBSVR_TYPE){
                    case DBSVR_MYSQL:
                        if(iStart == 0 && iRecordToGet == 0)
                            sql += "";
                        else
                            sql += " LIMIT "+iStart+", "+iRecordToGet;
                    break;
                    case DBSVR_POSTGRESQL:
                        if(iStart == 0 && iRecordToGet == 0)
                            sql += "";
                        else
                            sql += " LIMIT "+iRecordToGet+" OFFSET "+iStart;
                    break;
                    case DBSVR_ORACLE:
                    break;
                    case DBSVR_MSSQL:
                    break;
                    case DBSVR_SYBASE:
                    break;
                    default:
                    break;
                }
                
                System.out.println("SQL PstPaymentTerm.list() :::: "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    PaymentTerm objPaymentTerm = new PaymentTerm();
                    objPaymentTerm = resultToObject(rs, objPaymentTerm);
                    vResult.add(objPaymentTerm);
                }
                rs.close();
                return vResult;
            }catch(Exception e){
                System.out.println("Exception on "+new PstPaymentTerm().getClass().getName()+".list() ::: "+e.toString());
            }
        return new Vector(1,1);
    }
    
    public static PaymentTerm resultToObject(ResultSet rs, PaymentTerm objPaymentTerm){
        try{
            objPaymentTerm.setOID(rs.getLong(fieldNames[FLD_PAY_TERM_ID]));
            objPaymentTerm.setAmount(rs.getDouble(fieldNames[FLD_AMOUNT]));
            objPaymentTerm.setNote(rs.getString(fieldNames[FLD_NOTE]));
            objPaymentTerm.setPlanDate(rs.getDate(fieldNames[FLD_PLAN_DATE]));
            objPaymentTerm.setType(rs.getInt(fieldNames[FLD_TYPE]));
            objPaymentTerm.setInvoiceId(rs.getLong(fieldNames[FLD_INVOICE_ID]));
        }catch(Exception e){
            System.out.println("Exception on "+new PstPaymentTerm().getClass().getName()+".resultToObject() ::: "+e.toString());
        }
        return objPaymentTerm;
    }
    
    public static int getCount(String strWhClause){
        DBResultSet dbrs = null;
        int iResult = 0;
            try{
                String sql = " SELECT COUNT("+fieldNames[FLD_PAY_TERM_ID]+") FROM "+TBL_PAY_TERM;
                if(strWhClause != null && strWhClause.length() > 0)
                    sql += " WHERE "+strWhClause;
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    iResult = rs.getInt(1);
                }
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on "+new PstPaymentTerm().getClass().getName()+".getCount() ::: "+e.toString());
            }
        return iResult;
    }
    
    public static void main(String[] arg){
        PstPaymentTerm pstPaymentTerm = new PstPaymentTerm();
        PaymentTerm objPaymentTerm = new PaymentTerm();
        Date date = new Date();
        date.setMonth(0);
        /*objPaymentTerm.setAmount(2);
        objPaymentTerm.setNote("Payment II");
        objPaymentTerm.setPlanDate(date);
        objPaymentTerm.setType(0);
        PaymentTerm objPaymentT = new PaymentTerm();
        try{
            objPaymentT = (PaymentTerm)PstPaymentTerm.fetchExc(504404345512326536L);
        }catch(Exception e){}*/
        
        objPaymentTerm.setAmount(28000000);
        objPaymentTerm.setNote("Payment Invoice");
        objPaymentTerm.setPlanDate(date);
        objPaymentTerm.setType(1);
        objPaymentTerm.setInvoiceId(504404353118274052L);
        try{            
           long lOId = PstPaymentTerm.insertExc(objPaymentTerm);
        }catch(Exception e){
            System.out.println("INSERT GAGAL "+e.toString());
        }
    }
}

