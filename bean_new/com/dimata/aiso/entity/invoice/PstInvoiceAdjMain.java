/*
 * PstInvoiceAdjMain.java
 *
 * Created on November 12, 2007, 2:21 PM
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

public class PstInvoiceAdjMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_JournalType{
    
    public static final String TBL_INV_ADJ_MAIN = "aiso_inv_adj_main";
    
    public static final int FLD_ADJUSTMENT_ID = 0;
    public static final int FLD_DOC_NUMBER = 1;
    public static final int FLD_NOTE = 2;
    public static final int FLD_DATE = 3;
    public static final int FLD_STATUS = 4;
    public static final int FLD_INVOICE_ID = 5;
    public static final int FLD_NUMBER_COUNTER = 6;
    public static final int FLD_REFERENCE = 7;
    public static final int FLD_TYPE = 8;
    
    public static String[] fieldNames = {
        "ADJUSTMENT_ID",
        "DOC_NUMBER",
        "NOTE",
        "DATE",
        "STATUS",
        "INVOICE_ID",
        "NUMBER_COUNTER",
        "REFERENCE",
        "TYPE"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT
    };
    
    /** Creates a new instance of PstInvoiceAdjMain */
    public PstInvoiceAdjMain() {
    }
    
    public PstInvoiceAdjMain(int i) throws DBException{
        super(new PstInvoiceAdjMain());
    }
    
    public PstInvoiceAdjMain(String sOid) throws DBException{
        super(new PstInvoiceAdjMain(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
     public PstInvoiceAdjMain(long lOid) throws DBException{
        super(new PstInvoiceAdjMain(0));
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
        return deleteExc((InvoiceAdjMain)entity);
    }
    
    public long fetchExc(Entity entity) throws Exception {
        InvoiceAdjMain objInvoiceAdjMain = PstInvoiceAdjMain.fetchExc(entity.getOID());
        entity = (Entity)objInvoiceAdjMain;
        return objInvoiceAdjMain.getOID();
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
        return new PstInvoiceAdjMain().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_INV_ADJ_MAIN;
    }
    
    public long insertExc(Entity entity) throws Exception {
        return PstInvoiceAdjMain.insertExc((InvoiceAdjMain)entity);
    }
    
    public long updateExc(Entity entity) throws Exception {
        return PstInvoiceAdjMain.updateExc((InvoiceAdjMain)entity);
    }
    
    public static InvoiceAdjMain fetchExc(long lOid) throws DBException{
        try{
            InvoiceAdjMain objInvoiceAdjMain = new InvoiceAdjMain();
            PstInvoiceAdjMain pstInvoiceAdjMain = new PstInvoiceAdjMain(lOid);
            objInvoiceAdjMain.setOID(lOid);
            
            objInvoiceAdjMain.setDate(pstInvoiceAdjMain.getDate(FLD_DATE));
            objInvoiceAdjMain.setDocNumber(pstInvoiceAdjMain.getString(FLD_DOC_NUMBER));
            objInvoiceAdjMain.setInvoiceId(pstInvoiceAdjMain.getlong(FLD_INVOICE_ID));
            objInvoiceAdjMain.setNote(pstInvoiceAdjMain.getString(FLD_NOTE));
            objInvoiceAdjMain.setNumberCounter(pstInvoiceAdjMain.getInt(FLD_NUMBER_COUNTER));
            objInvoiceAdjMain.setReference(pstInvoiceAdjMain.getString(FLD_REFERENCE));
            objInvoiceAdjMain.setStatus(pstInvoiceAdjMain.getInt(FLD_STATUS));
            objInvoiceAdjMain.setType(pstInvoiceAdjMain.getInt(FLD_TYPE));
            
            return objInvoiceAdjMain;            
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceAdjMain(0), DBException.UNKNOWN);
        }    
    }
    
    public static long insertExc(InvoiceAdjMain objInvoiceAdjMain) throws DBException{
        try{
            PstInvoiceAdjMain pstInvoiceAdjMain = new PstInvoiceAdjMain(0);
            
            pstInvoiceAdjMain.setDate(FLD_DATE, objInvoiceAdjMain.getDate());
            pstInvoiceAdjMain.setString(FLD_DOC_NUMBER, objInvoiceAdjMain.getDocNumber());
            pstInvoiceAdjMain.setLong(FLD_INVOICE_ID, objInvoiceAdjMain.getInvoiceId());
            pstInvoiceAdjMain.setString(FLD_NOTE, objInvoiceAdjMain.getNote());
            pstInvoiceAdjMain.setInt(FLD_NUMBER_COUNTER, objInvoiceAdjMain.getNumberCounter());
            pstInvoiceAdjMain.setString(FLD_REFERENCE, objInvoiceAdjMain.getReference());
            pstInvoiceAdjMain.setInt(FLD_STATUS, objInvoiceAdjMain.getStatus());
            pstInvoiceAdjMain.setInt(FLD_TYPE, objInvoiceAdjMain.getType());            
            
            pstInvoiceAdjMain.insert();
            objInvoiceAdjMain.setOID(pstInvoiceAdjMain.getlong(FLD_ADJUSTMENT_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceAdjMain(0), DBException.UNKNOWN);
        }
        return objInvoiceAdjMain.getOID();
    }
    
    public static long updateExc(InvoiceAdjMain objInvoiceAdjMain) throws DBException{
        try{
            if(objInvoiceAdjMain.getOID() != 0){
                PstInvoiceAdjMain pstInvoiceAdjMain = new PstInvoiceAdjMain(objInvoiceAdjMain.getOID());
                
                pstInvoiceAdjMain.setDate(FLD_DATE, objInvoiceAdjMain.getDate());
                pstInvoiceAdjMain.setString(FLD_DOC_NUMBER, objInvoiceAdjMain.getDocNumber());
                pstInvoiceAdjMain.setLong(FLD_INVOICE_ID, objInvoiceAdjMain.getInvoiceId());
                pstInvoiceAdjMain.setString(FLD_NOTE, objInvoiceAdjMain.getNote());
                pstInvoiceAdjMain.setInt(FLD_NUMBER_COUNTER, objInvoiceAdjMain.getNumberCounter());
                pstInvoiceAdjMain.setString(FLD_REFERENCE, objInvoiceAdjMain.getReference());
                pstInvoiceAdjMain.setInt(FLD_STATUS, objInvoiceAdjMain.getStatus());
                pstInvoiceAdjMain.setInt(FLD_TYPE, objInvoiceAdjMain.getType());            
            
                pstInvoiceAdjMain.update();
                return objInvoiceAdjMain.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceAdjMain(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstInvoiceAdjMain pstInvoiceAdjMain = new PstInvoiceAdjMain(lOid);
            pstInvoiceAdjMain.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceAdjMain(0), DBException.UNKNOWN);
        }
        return lOid;
    }
    
    public static Vector list(int iStart, int iRecordToGet, String strWhClause, String strOrderBy){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
            try{
                String sql = " SELECT * FROM "+TBL_INV_ADJ_MAIN;
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
                    case DBSVR_SYBASE:                       
                    break;
                    case DBSVR_MSSQL:                       
                    break;
                    default:                       
                    break;
                }
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    InvoiceAdjMain objInvoiceAdjMain = new InvoiceAdjMain();
                    objInvoiceAdjMain = resultToObject(rs, objInvoiceAdjMain);
                    vResult.add(objInvoiceAdjMain);
                }
                rs.close();
                return vResult;                
            }catch(Exception e){
                System.out.println("Exception on "+new PstInvoiceAdjMain().getClass().getName()+".list() ::: "+e.toString());
            }
        return new Vector(1,1);
    }
    
    public static InvoiceAdjMain resultToObject(ResultSet rs, InvoiceAdjMain objInvoiceAdjMain){
        try{
            objInvoiceAdjMain.setOID(rs.getLong(fieldNames[FLD_ADJUSTMENT_ID]));
            objInvoiceAdjMain.setDate(rs.getDate(fieldNames[FLD_DATE]));
            objInvoiceAdjMain.setDocNumber(rs.getString(fieldNames[FLD_DOC_NUMBER]));
            objInvoiceAdjMain.setInvoiceId(rs.getLong(fieldNames[FLD_INVOICE_ID]));
            objInvoiceAdjMain.setNote(rs.getString(fieldNames[FLD_NOTE]));
            objInvoiceAdjMain.setNumberCounter(rs.getInt(fieldNames[FLD_NUMBER_COUNTER]));
            objInvoiceAdjMain.setReference(rs.getString(fieldNames[FLD_REFERENCE]));
            objInvoiceAdjMain.setStatus(rs.getInt(fieldNames[FLD_STATUS]));
            objInvoiceAdjMain.setType(rs.getInt(fieldNames[FLD_TYPE]));
        }catch(Exception e){
            System.out.println("Exception on "+new PstInvoiceAdjMain().getClass().getName()+".resultToObject() ::: "+e.toString());
        }
        return objInvoiceAdjMain;
    }
    
    public static int getCount(String strWhClause){
        DBResultSet dbrs = null;
        int iResult = 0;
            try{
                String sql = " SELECT COUNT("+fieldNames[FLD_ADJUSTMENT_ID]+") FROM "+TBL_INV_ADJ_MAIN;
                if(strWhClause != null && strWhClause.length() > 0)
                    sql += " WHERE "+strWhClause;
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    iResult = rs.getInt(1);
                }
                rs.close();
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on "+new PstInvoiceAdjMain().getClass().getName()+".getCount() ::: "+e.toString());
            }
        return iResult;
    }
    
    public static void main(String[] arg){
        PstInvoiceAdjMain pstInvoiceAdjMain = new PstInvoiceAdjMain();
        InvoiceAdjMain objInvAdjMain = new InvoiceAdjMain();
        
        objInvAdjMain.setDate(new Date());
        objInvAdjMain.setDocNumber("321465132");
        objInvAdjMain.setInvoiceId(504404353118274052L);
        objInvAdjMain.setNote("Adjustment Invoice No 0708-050");
        objInvAdjMain.setNumberCounter(1);
        objInvAdjMain.setReference("0708-050");
        objInvAdjMain.setStatus(0);
        objInvAdjMain.setType(0);
        
        try{
            long lInsert = pstInvoiceAdjMain.insertExc(objInvAdjMain);            
            System.out.println("SUKSES ::: ");
        }catch(Exception e){
             System.out.println("GAGAL");
             e.printStackTrace();
        }        
    }
}

