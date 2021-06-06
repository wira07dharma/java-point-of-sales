/*
 * PstInvoiceDetail.java
 *
 * Created on November 12, 2007, 2:23 PM
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

public class PstInvoiceDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_JournalType{
    
    public static final String TBL_INV_DETAIL = "aiso_inv_detail";
    public static final int FLD_INV_DETAIL_ID = 0;
    public static final int FLD_NAME_OF_PAX = 1;
    public static final int FLD_DESCRIPTION = 2;
    public static final int FLD_DATE = 3;
    public static final int FLD_NUMBER = 4;
    public static final int FLD_UNIT_PRICE = 5;
    public static final int FLD_ITEM_DISCOUNT = 6;
    public static final int FLD_INVOICE_ID = 7;
    public static final int FLD_ID_PERKIRAAN = 8;
    
    public static String[] fieldNames = {
        "INVOICE_DETAIL_ID",
        "NAME_OF_PAX",
        "DESCRIPTION",
        "DATE",
        "NUMBER",
        "UNIT_PRICE",
        "ITEM_DISCOUNT",
        "INVOICE_ID",
        "ID_PERKIRAAN"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG,
        TYPE_LONG
    };
    
    /** Creates a new instance of PstInvoiceDetail */
    public PstInvoiceDetail() {
    }
    
    public PstInvoiceDetail(int i)throws DBException {
         super(new PstInvoiceDetail());
    }
    
    public PstInvoiceDetail(String sOid)throws DBException {
         super(new PstInvoiceDetail(0));
         if(!locate(sOid))
             throw new DBException(this, DBException.RECORD_NOT_FOUND);
         else
             return;
    }
    
     public PstInvoiceDetail(long lOid)throws DBException {
         super(new PstInvoiceDetail(0));
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
        return deleteExc((InvoiceDetail)entity);
    }
    
    public long fetchExc(Entity entity) throws Exception {
        InvoiceDetail objInvoiceDetail = PstInvoiceDetail.fetchExc(entity.getOID());
        entity = (Entity)objInvoiceDetail;
        return objInvoiceDetail.getOID();
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
        return new PstInvoiceDetail().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_INV_DETAIL;
    }
    
    public long insertExc(Entity entity) throws Exception {
        return PstInvoiceDetail.insertExc((InvoiceDetail)entity);
    }
    
    public long updateExc(Entity entity) throws Exception {
        return PstInvoiceDetail.updateExc((InvoiceDetail)entity);
    }
    
    public static InvoiceDetail fetchExc(long lOid) throws DBException{
        try{
            InvoiceDetail objInvoiceDetail = new InvoiceDetail();
            PstInvoiceDetail pstInvoiceDetail = new PstInvoiceDetail(lOid);            
            objInvoiceDetail.setOID(lOid);
            
            objInvoiceDetail.setDate(pstInvoiceDetail.getDate(FLD_DATE));
            objInvoiceDetail.setDescription(pstInvoiceDetail.getString(FLD_DESCRIPTION));
            objInvoiceDetail.setIdPerkiraan(pstInvoiceDetail.getlong(FLD_ID_PERKIRAAN));
            objInvoiceDetail.setInvoiceId(pstInvoiceDetail.getlong(FLD_INVOICE_ID));
            objInvoiceDetail.setItemDiscount(pstInvoiceDetail.getdouble(FLD_ITEM_DISCOUNT));
            objInvoiceDetail.setNameOfPax(pstInvoiceDetail.getString(FLD_NAME_OF_PAX));
            objInvoiceDetail.setNumber(pstInvoiceDetail.getInt(FLD_NUMBER));
            objInvoiceDetail.setUnitPrice(pstInvoiceDetail.getdouble(FLD_UNIT_PRICE));
            
            return objInvoiceDetail;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceDetail(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(InvoiceDetail objInvoiceDetail) throws DBException{
        try{
            PstInvoiceDetail pstInvoiceDetail = new PstInvoiceDetail(0);
            
            pstInvoiceDetail.setDate(FLD_DATE, objInvoiceDetail.getDate());
            pstInvoiceDetail.setString(FLD_DESCRIPTION, objInvoiceDetail.getDescription());
            pstInvoiceDetail.setLong(FLD_ID_PERKIRAAN, objInvoiceDetail.getIdPerkiraan());
            pstInvoiceDetail.setLong(FLD_INVOICE_ID, objInvoiceDetail.getInvoiceId());
            pstInvoiceDetail.setDouble(FLD_ITEM_DISCOUNT, objInvoiceDetail.getItemDiscount());
            pstInvoiceDetail.setString(FLD_NAME_OF_PAX, objInvoiceDetail.getNameOfPax());
            pstInvoiceDetail.setInt(FLD_NUMBER, objInvoiceDetail.getNumber());
            pstInvoiceDetail.setDouble(FLD_UNIT_PRICE, objInvoiceDetail.getUnitPrice());
            
            pstInvoiceDetail.insert();
            objInvoiceDetail.setOID(pstInvoiceDetail.getlong(FLD_INV_DETAIL_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceDetail(0), DBException.UNKNOWN);
        }
        return objInvoiceDetail.getOID();
    }
    
    public static long updateExc(InvoiceDetail objInvoiceDetail) throws DBException{
        try{
            if(objInvoiceDetail.getOID() != 0){
                PstInvoiceDetail pstInvoiceDetail = new PstInvoiceDetail(objInvoiceDetail.getOID());
                
                pstInvoiceDetail.setDate(FLD_DATE, objInvoiceDetail.getDate());
                pstInvoiceDetail.setString(FLD_DESCRIPTION, objInvoiceDetail.getDescription());
                pstInvoiceDetail.setLong(FLD_ID_PERKIRAAN, objInvoiceDetail.getIdPerkiraan());
                pstInvoiceDetail.setLong(FLD_INVOICE_ID, objInvoiceDetail.getInvoiceId());
                pstInvoiceDetail.setDouble(FLD_ITEM_DISCOUNT, objInvoiceDetail.getItemDiscount());
                pstInvoiceDetail.setString(FLD_NAME_OF_PAX, objInvoiceDetail.getNameOfPax());
                pstInvoiceDetail.setInt(FLD_NUMBER, objInvoiceDetail.getNumber());
                pstInvoiceDetail.setDouble(FLD_UNIT_PRICE, objInvoiceDetail.getUnitPrice());
                
                pstInvoiceDetail.update();
                return objInvoiceDetail.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceDetail(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstInvoiceDetail pstInvoiceDetail = new PstInvoiceDetail(lOid);
            pstInvoiceDetail.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceDetail(), DBException.UNKNOWN);
        }
        return lOid;
    }
    
    public static Vector list(int iStart, int iRecordToGet, String strWhClause, String strOrderBy){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT * FROM "+TBL_INV_DETAIL;
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
                InvoiceDetail objInvoiceDetail = new InvoiceDetail();
                objInvoiceDetail = resultToObject(rs, objInvoiceDetail);
                vResult.add(objInvoiceDetail);
            }
            rs.close();
            return vResult;
        }catch(Exception e){
            System.out.println("Exception on "+new PstInvoiceDetail().getClass().getName()+".list() ::: "+e.toString());
        }
        return new Vector(1,1);
    }
    
    public static InvoiceDetail resultToObject(ResultSet rs, InvoiceDetail objInvoiceDetail){
        try{
            objInvoiceDetail.setOID(rs.getLong(fieldNames[FLD_INV_DETAIL_ID]));
            objInvoiceDetail.setDate(rs.getDate(fieldNames[FLD_DATE]));
            objInvoiceDetail.setDescription(rs.getString(fieldNames[FLD_DESCRIPTION]));
            objInvoiceDetail.setIdPerkiraan(rs.getLong(fieldNames[FLD_ID_PERKIRAAN]));
            objInvoiceDetail.setInvoiceId(rs.getLong(fieldNames[FLD_INVOICE_ID]));
            objInvoiceDetail.setItemDiscount(rs.getDouble(fieldNames[FLD_ITEM_DISCOUNT]));
            objInvoiceDetail.setNameOfPax(rs.getString(fieldNames[FLD_NAME_OF_PAX]));
            objInvoiceDetail.setNumber(rs.getInt(fieldNames[FLD_NUMBER]));
            objInvoiceDetail.setUnitPrice(rs.getDouble(fieldNames[FLD_UNIT_PRICE]));
        }catch(Exception e){
            System.out.println("Exception on "+new PstInvoiceDetail().getClass().getName()+".resultToObject ::: "+e.toString());
        }
        return objInvoiceDetail;
    }
    
    public static int getCount(String strWhClause){
        DBResultSet dbrs = null;
        int iResult = 0;
            try{
                String sql = " SELECT COUNT("+fieldNames[FLD_INV_DETAIL_ID]+") FROM "+TBL_INV_DETAIL;
                if(strWhClause != null && strWhClause.length() > 0)
                    sql += " WHERE "+strWhClause;
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    iResult = rs.getInt(1);
                }
            }catch(Exception e){
                System.out.println("Exception on "+new PstInvoiceDetail().getClass().getName()+".getCount() ::: "+e.toString());
            }
        return iResult;
    }
    
    
    public static void main(String[] arg){
        PstInvoiceDetail pstInvoiceDetail = new PstInvoiceDetail();
        InvoiceDetail objInvoiceDetail = new InvoiceDetail();
       
        objInvoiceDetail.setDate(new Date());
        objInvoiceDetail.setDescription("Undangan Nikah");
        objInvoiceDetail.setIdPerkiraan(504404666020321339L);
        objInvoiceDetail.setInvoiceId(504404353118477239L);
        objInvoiceDetail.setItemDiscount(0);
        objInvoiceDetail.setNameOfPax("Arman Maulana");
        objInvoiceDetail.setNumber(1);
        objInvoiceDetail.setUnitPrice(950000);        
        
        try{
            long lOid = pstInvoiceDetail.insertExc(objInvoiceDetail);            
            System.out.println("SUKSES :::: ");
        }catch(Exception e){
            System.out.println("GAGAL"+e.toString());
        }
    }
}
