/*
 * PstInvoiceAdjDetail.java
 *
 * Created on November 12, 2007, 2:19 PM
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

public class PstInvoiceAdjDetail extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_JournalType{
    
    public static final String TBL_INV_ADJ_DETAIL = "aiso_inv_adj_detail";
    
    public static final int FLD_ADJ_DETAIL_ID = 0;
    public static final int FLD_NAME_OF_PAX = 1;
    public static final int FLD_DESCRIPTION = 2;
    public static final int FLD_DATE = 3;
    public static final int FLD_NUMBER = 4;
    public static final int FLD_UNIT_PRICE = 5;
    public static final int FLD_ITEM_DISCOUNT = 6;
    public static final int FLD_ADJUSTMENT_ID = 7;
    public static final int FLD_ID_PERKIRAAN = 8;
    
    public static String[] fieldNames = {
        "ADJ_DETAIL_ID",
        "NAME_OF_PAX",
        "DESCRIPTION",
        "DATE",
        "NUMBER",
        "UNIT_PRICE",
        "ITEM_DISCOUNT",
        "ADJUSTMENT_ID",
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
    
    /** Creates a new instance of PstInvoiceAdjDetail */
    public PstInvoiceAdjDetail() {
    }
    
    public PstInvoiceAdjDetail(int i) throws DBException {
        super(new PstInvoiceAdjDetail());
    }
    
     public PstInvoiceAdjDetail(String sOid) throws DBException {
        super(new PstInvoiceAdjDetail(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
     
     public PstInvoiceAdjDetail(long lOid) throws DBException {
        super(new PstInvoiceAdjDetail(0));
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
        return deleteExc((InvoiceAdjDetail)entity);
    }
    
    public long fetchExc(Entity entity) throws Exception {
        InvoiceAdjDetail objInvoiceAdjDetail = PstInvoiceAdjDetail.fetchExc(entity.getOID());
        entity = (Entity)objInvoiceAdjDetail;
        return objInvoiceAdjDetail.getOID();
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
        return new PstInvoiceAdjDetail().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_INV_ADJ_DETAIL;
    }
    
    public long insertExc(Entity entity) throws Exception {
        return PstInvoiceAdjDetail.insertExc((InvoiceAdjDetail)entity);
    }
    
    public long updateExc(Entity entity) throws Exception {
        return PstInvoiceAdjDetail.updateExc((InvoiceAdjDetail)entity);
    }
    
    public static InvoiceAdjDetail fetchExc(long lOid) throws DBException{
        try{
            InvoiceAdjDetail objInvoiceAdjDetail = new InvoiceAdjDetail();
            PstInvoiceAdjDetail pstInvoiceAdjDetail = new PstInvoiceAdjDetail(lOid);
            objInvoiceAdjDetail.setOID(lOid);
            
            objInvoiceAdjDetail.setAdjustmentId(pstInvoiceAdjDetail.getlong(FLD_ADJUSTMENT_ID));
            objInvoiceAdjDetail.setDate(pstInvoiceAdjDetail.getDate(FLD_DATE));
            objInvoiceAdjDetail.setDescription(pstInvoiceAdjDetail.getString(FLD_DESCRIPTION));
            objInvoiceAdjDetail.setIdPerkiraan(pstInvoiceAdjDetail.getlong(FLD_ID_PERKIRAAN));
            objInvoiceAdjDetail.setItemDiscount(pstInvoiceAdjDetail.getdouble(FLD_ITEM_DISCOUNT));
            objInvoiceAdjDetail.setNameOfPax(pstInvoiceAdjDetail.getString(FLD_NAME_OF_PAX));
            objInvoiceAdjDetail.setNumber(pstInvoiceAdjDetail.getInt(FLD_NUMBER));
            objInvoiceAdjDetail.setUnitPrice(pstInvoiceAdjDetail.getdouble(FLD_UNIT_PRICE));
            
            return objInvoiceAdjDetail;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceAdjDetail(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(InvoiceAdjDetail objInvoiceAdjDetail) throws DBException{
        try{
            PstInvoiceAdjDetail pstInvoiceAdjDetail = new PstInvoiceAdjDetail(0);
            
            pstInvoiceAdjDetail.setLong(FLD_ADJUSTMENT_ID, objInvoiceAdjDetail.getAdjustmentId());
            pstInvoiceAdjDetail.setDate(FLD_DATE, objInvoiceAdjDetail.getDate());
            pstInvoiceAdjDetail.setString(FLD_DESCRIPTION, objInvoiceAdjDetail.getDescription());
            pstInvoiceAdjDetail.setLong(FLD_ID_PERKIRAAN, objInvoiceAdjDetail.getIdPerkiraan());
            pstInvoiceAdjDetail.setDouble(FLD_ITEM_DISCOUNT, objInvoiceAdjDetail.getItemDiscount());
            pstInvoiceAdjDetail.setString(FLD_NAME_OF_PAX, objInvoiceAdjDetail.getNameOfPax());
            pstInvoiceAdjDetail.setInt(FLD_NUMBER, objInvoiceAdjDetail.getNumber());
            pstInvoiceAdjDetail.setDouble(FLD_UNIT_PRICE, objInvoiceAdjDetail.getUnitPrice());
            
            pstInvoiceAdjDetail.insert();
            objInvoiceAdjDetail.setOID(pstInvoiceAdjDetail.getlong(FLD_ADJ_DETAIL_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceAdjDetail(0), DBException.UNKNOWN);
        }
        return objInvoiceAdjDetail.getOID();
    }
    
    public static long updateExc(InvoiceAdjDetail objInvoiceAdjDetail) throws DBException{
        try{
            if(objInvoiceAdjDetail.getOID() != 0){
                PstInvoiceAdjDetail pstInvoiceAdjDetail = new PstInvoiceAdjDetail(objInvoiceAdjDetail.getOID());
                
                pstInvoiceAdjDetail.setLong(FLD_ADJUSTMENT_ID, objInvoiceAdjDetail.getAdjustmentId());
                pstInvoiceAdjDetail.setDate(FLD_DATE, objInvoiceAdjDetail.getDate());
                pstInvoiceAdjDetail.setString(FLD_DESCRIPTION, objInvoiceAdjDetail.getDescription());
                pstInvoiceAdjDetail.setLong(FLD_ID_PERKIRAAN, objInvoiceAdjDetail.getIdPerkiraan());
                pstInvoiceAdjDetail.setDouble(FLD_ITEM_DISCOUNT, objInvoiceAdjDetail.getItemDiscount());
                pstInvoiceAdjDetail.setString(FLD_NAME_OF_PAX, objInvoiceAdjDetail.getNameOfPax());
                pstInvoiceAdjDetail.setInt(FLD_NUMBER, objInvoiceAdjDetail.getNumber());
                pstInvoiceAdjDetail.setDouble(FLD_UNIT_PRICE, objInvoiceAdjDetail.getUnitPrice());
                
                pstInvoiceAdjDetail.update();
                return objInvoiceAdjDetail.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceAdjDetail(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstInvoiceAdjDetail pstInvoiceAdjDetail = new PstInvoiceAdjDetail(lOid);
            pstInvoiceAdjDetail.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstInvoiceAdjDetail(0), DBException.UNKNOWN);
        }
        return lOid;
    }
    
    public static Vector list(int iStart, int iRecordToGet, String strWhClause, String strOrderBy){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
            try{
                String sql = " SELECT * FROM "+TBL_INV_ADJ_DETAIL;
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
                    InvoiceAdjDetail objInvoiceAdjDetail = new InvoiceAdjDetail();
                    objInvoiceAdjDetail = resultToObject(rs, objInvoiceAdjDetail);
                    vResult.add(objInvoiceAdjDetail);
                }
                rs.close();
                return vResult;
            }catch(Exception e){
                System.out.println("Exception on "+new PstInvoiceAdjDetail().getClass().getName()+".list() ::: "+e.toString());
            }
        return new Vector(1,1);
    }
    
    public static InvoiceAdjDetail resultToObject(ResultSet rs, InvoiceAdjDetail objInvoiceAdjDetail){
        try{
            objInvoiceAdjDetail.setOID(rs.getLong(fieldNames[FLD_ADJ_DETAIL_ID]));
            objInvoiceAdjDetail.setAdjustmentId(rs.getLong(fieldNames[FLD_ADJUSTMENT_ID]));
            objInvoiceAdjDetail.setDate(rs.getDate(fieldNames[FLD_DATE]));
            objInvoiceAdjDetail.setDescription(rs.getString(fieldNames[FLD_DESCRIPTION]));
            objInvoiceAdjDetail.setIdPerkiraan(rs.getLong(fieldNames[FLD_ID_PERKIRAAN]));
            objInvoiceAdjDetail.setItemDiscount(rs.getDouble(fieldNames[FLD_ITEM_DISCOUNT]));
            objInvoiceAdjDetail.setNameOfPax(rs.getString(fieldNames[FLD_NAME_OF_PAX]));
            objInvoiceAdjDetail.setNumber(rs.getInt(fieldNames[FLD_NUMBER]));
            objInvoiceAdjDetail.setUnitPrice(rs.getDouble(fieldNames[FLD_UNIT_PRICE]));
            
        }catch(Exception e){
            System.out.println("Exception on "+new PstInvoiceAdjDetail().getClass().getName()+".resultToObject() ::: "+e.toString());
        }
        return objInvoiceAdjDetail;
    }
    
    public static int getCount(String strWhClause){
        DBResultSet dbrs = null;
        int iResult = 0;
            try{
                String sql = " SELECT COUNT("+fieldNames[FLD_ADJ_DETAIL_ID]+") FROM "+TBL_INV_ADJ_DETAIL;
                if(strWhClause != null && strWhClause.length() > 0)
                    sql += " WHERE "+strWhClause;
                dbrs = DBHandler.execQueryResult(sql); 
                ResultSet rs = dbrs.getResultSet();
                 while(rs.next()){
                    iResult = rs.getInt(1);
                 }
                 rs.close();
            }catch(Exception e){
                System.out.println("Exception on "+new PstInvoiceAdjDetail().getClass().getName()+".getCount() ::: "+e.toString());
            }
        return iResult;
    }
    
    public static void main(String[] arg){
        PstInvoiceAdjDetail pstInvoiceAdjDetail = new PstInvoiceAdjDetail();
        InvoiceAdjDetail objInvoiceAdjDetail = new InvoiceAdjDetail();
        try{
            objInvoiceAdjDetail = pstInvoiceAdjDetail.fetchExc(504404353119878145L);
        }catch(Exception e){}
        objInvoiceAdjDetail.setAdjustmentId(504404353119472755L);
        objInvoiceAdjDetail.setDate(new Date());
        objInvoiceAdjDetail.setDescription("Adjustment harga an Reinald Gazali");
        objInvoiceAdjDetail.setIdPerkiraan(504404353119472755L);
        objInvoiceAdjDetail.setItemDiscount(10.25);
        objInvoiceAdjDetail.setNameOfPax("Reinald Gazali");
        objInvoiceAdjDetail.setNumber(1);
        objInvoiceAdjDetail.setUnitPrice(6000000);
        
        try{
            long lUpdate = pstInvoiceAdjDetail.updateExc(objInvoiceAdjDetail);            
            System.out.println("SUKSES iCount :::: ");
        }catch(Exception e){
            System.out.println("GAGAL ::: "+e.toString());
            e.printStackTrace();
        }
    }
}

