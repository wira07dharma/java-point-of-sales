/*
 * PstCostingAdjustment.java
 *
 * Created on November 12, 2007, 2:10 PM
 */

package com.dimata.aiso.entity.costing;

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

public class PstCostingAdjustment extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_JournalType{
    
    public static final String TBL_COSTING_ADJ = "aiso_costing_adj";
    
    public static final int FLD_COSTING_ADJ_ID = 0;
    public static final int FLD_DESCRIPTION = 1;
    public static final int FLD_NUMBER = 2;
    public static final int FLD_UNIT_PRICE = 3;
    public static final int FLD_DISCOUNT = 4;
    public static final int FLD_STATUS = 5;
    public static final int FLD_CONTACT_ID = 6;
    public static final int FLD_REFERENCE = 7;
    public static final int FLD_COSTING_ID = 8;
    
    public static String[] fieldNames = {
        "COSTING_ADJ_ID",
        "DESCRIPTION",
        "NUMBER",
        "UNIT_PRICE",
        "DISCOUNT",
        "STATUS",
        "CONTACT_ID",
        "REFERENCE",
        "COSTING_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG
    };
    
    /** Creates a new instance of PstCostingAdjustment */
    public PstCostingAdjustment() {
    }
    
    public PstCostingAdjustment(int i) throws DBException {
        super(new PstCostingAdjustment());
    }
    
    public PstCostingAdjustment(String sOid) throws DBException {
        super(new PstCostingAdjustment(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
     public PstCostingAdjustment(long lOid) throws DBException {
        super(new PstCostingAdjustment(0));
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
        return new PstCostingAdjustment().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_COSTING_ADJ;
    }
    
    public long deleteExc(Entity entity) throws Exception {
        return deleteExc((CostingAdjustment)entity);
    }
    
    public long fetchExc(Entity entity) throws Exception {
        CostingAdjustment objCostingAdj = PstCostingAdjustment.fetchExc(entity.getOID());
        entity = (Entity)objCostingAdj;
        return objCostingAdj.getOID();
    }
    
    public long insertExc(Entity entity) throws Exception {
        return insertExc((CostingAdjustment)entity);
    }
    
    public long updateExc(Entity entity) throws Exception {
        return updateExc((CostingAdjustment)entity);
    }
    
    public static CostingAdjustment fetchExc(long lOid) throws DBException{
        try{
            CostingAdjustment objCostingAdj = new CostingAdjustment();
            PstCostingAdjustment pstCostingAdj = new PstCostingAdjustment(lOid);
            objCostingAdj.setOID(lOid);
            
            objCostingAdj.setContactId(pstCostingAdj.getlong(FLD_CONTACT_ID));
            objCostingAdj.setCostingId(pstCostingAdj.getlong(FLD_COSTING_ID));
            objCostingAdj.setDescription(pstCostingAdj.getString(FLD_DESCRIPTION));
            objCostingAdj.setDiscount(pstCostingAdj.getdouble(FLD_DISCOUNT));
            objCostingAdj.setNumber(pstCostingAdj.getInt(FLD_NUMBER));
            objCostingAdj.setReference(pstCostingAdj.getString(FLD_REFERENCE));
            objCostingAdj.setStatus(pstCostingAdj.getInt(FLD_STATUS));
            objCostingAdj.setUnitPrice(pstCostingAdj.getdouble(FLD_UNIT_PRICE));
            
            return objCostingAdj;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCostingAdjustment(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(CostingAdjustment objCostingAdj) throws DBException{
        try{
            PstCostingAdjustment pstCostingAdj = new PstCostingAdjustment(0);
            
            pstCostingAdj.setLong(FLD_CONTACT_ID, objCostingAdj.getContactId());
            pstCostingAdj.setLong(FLD_COSTING_ID, objCostingAdj.getCostingId());
            pstCostingAdj.setString(FLD_DESCRIPTION, objCostingAdj.getDescription());
            pstCostingAdj.setDouble(FLD_DISCOUNT, objCostingAdj.getDiscount());
            pstCostingAdj.setInt(FLD_NUMBER, objCostingAdj.getNumber());
            pstCostingAdj.setString(FLD_REFERENCE, objCostingAdj.getReference());
            pstCostingAdj.setInt(FLD_STATUS, objCostingAdj.getStatus());
            pstCostingAdj.setDouble(FLD_UNIT_PRICE, objCostingAdj.getUnitPrice());
            
            pstCostingAdj.insert();
            objCostingAdj.setOID(pstCostingAdj.getlong(FLD_COSTING_ADJ_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCostingAdjustment(0), DBException.UNKNOWN);
        }
        return objCostingAdj.getOID();
    }
    
    public static long updateExc(CostingAdjustment objCostingAdj) throws DBException{
        try{
            if(objCostingAdj.getOID() != 0){
                PstCostingAdjustment pstCostingAdj = new PstCostingAdjustment(objCostingAdj.getOID());
                
                pstCostingAdj.setLong(FLD_CONTACT_ID, objCostingAdj.getContactId());
                pstCostingAdj.setLong(FLD_COSTING_ID, objCostingAdj.getCostingId());
                pstCostingAdj.setString(FLD_DESCRIPTION, objCostingAdj.getDescription());
                pstCostingAdj.setDouble(FLD_DISCOUNT, objCostingAdj.getDiscount());
                pstCostingAdj.setInt(FLD_NUMBER, objCostingAdj.getNumber());
                pstCostingAdj.setString(FLD_REFERENCE, objCostingAdj.getReference());
                pstCostingAdj.setInt(FLD_STATUS, objCostingAdj.getStatus());
                pstCostingAdj.setDouble(FLD_UNIT_PRICE, objCostingAdj.getUnitPrice());
            
                pstCostingAdj.update();
                return objCostingAdj.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCostingAdjustment(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstCostingAdjustment pstCostingAdj = new PstCostingAdjustment(lOid);
            pstCostingAdj.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCostingAdjustment(0), DBException.UNKNOWN);
        }
        return lOid;
    }
    
    public static Vector list(int iStart, int iRecordToGet, String strWhClause, String strOrderBy){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
        try{
            String sql = " SELECT * FROM "+TBL_COSTING_ADJ;
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
                CostingAdjustment objCostingAdj = new CostingAdjustment();
                objCostingAdj = resultToObject(rs, objCostingAdj);
                vResult.add(objCostingAdj);
            }
            rs.close();
            return vResult;
        }catch(Exception e){
            System.out.println("Exception on "+new PstCostingAdjustment().getClass().getName()+".list() ::: "+e.toString());
        }
        return new Vector(1,1);
    }
    
    public static CostingAdjustment resultToObject(ResultSet rs, CostingAdjustment objCostingAdj){
        try{
            objCostingAdj.setOID(rs.getLong(fieldNames[FLD_COSTING_ADJ_ID]));
            objCostingAdj.setContactId(rs.getLong(fieldNames[FLD_CONTACT_ID]));
            objCostingAdj.setCostingId(rs.getLong(fieldNames[FLD_COSTING_ID]));
            objCostingAdj.setDescription(rs.getString(fieldNames[FLD_DESCRIPTION]));
            objCostingAdj.setDiscount(rs.getDouble(fieldNames[FLD_DISCOUNT]));
            objCostingAdj.setNumber(rs.getInt(fieldNames[FLD_NUMBER]));       
            objCostingAdj.setReference(rs.getString(fieldNames[FLD_REFERENCE]));
            objCostingAdj.setStatus(rs.getInt(fieldNames[FLD_STATUS]));
            objCostingAdj.setUnitPrice(rs.getDouble(fieldNames[FLD_UNIT_PRICE]));
        }catch(Exception e){
            System.out.println("Exception on "+new PstCostingAdjustment().getClass().getName()+".resultToObject() ::: "+e.toString());
        }
        return objCostingAdj;
    }
    
    public static int getCount(String strWhClause){
        DBResultSet dbrs = null;
        int iResult = 0;
        try{
            String sql = " SELECT COUNT("+fieldNames[FLD_COSTING_ADJ_ID]+") FROM "+TBL_COSTING_ADJ;
            if(strWhClause != null && strWhClause.length() > 0)
                sql += " WHERE "+strWhClause;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                iResult = rs.getInt(1);
            }
        }catch(Exception e){
            iResult = 0;
            System.out.println("Exception on "+new PstCostingAdjustment().getClass().getName()+".getCount() :::: "+e.toString());
        }
        return iResult;
    } 
    
    public static void main(String[] arg){
        PstCostingAdjustment pstCostingAdj = new PstCostingAdjustment();
        CostingAdjustment objCostingAdj = new CostingAdjustment();
        
        try{
            objCostingAdj = pstCostingAdj.fetchExc(504404345862911927L);
        }catch(Exception e){}
        objCostingAdj.setCostingId(504404353197167958L);
        objCostingAdj.setDescription("Adjustment Costing III ");
        objCostingAdj.setDiscount(10.45);
        objCostingAdj.setNumber(3);
        objCostingAdj.setReference("0708-050");
        objCostingAdj.setStatus(0);
        objCostingAdj.setUnitPrice(150000);
        objCostingAdj.setContactId(0);
        
        try{
            long lOid = pstCostingAdj.insertExc(objCostingAdj);
            System.out.println("SUKSES ::: ");
        }catch(Exception e){
            System.out.println("GAGAL ::: "+e.toString());
        }
    }
}

