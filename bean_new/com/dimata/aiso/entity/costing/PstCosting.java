/*
 * PstCosting.java
 *
 * Created on November 12, 2007, 2:09 PM
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

public class PstCosting extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_JournalType{
    
    public static final String TBL_COSTING = "aiso_costing";
    public static final int FLD_COSTING_ID = 0;
    public static final int FLD_DESCRIPTION = 1;
    public static final int FLD_NUMBER = 2;
    public static final int FLD_UNIT_PRICE = 3;
    public static final int FLD_DISCOUNT = 4;
    public static final int FLD_STATUS = 5;
    public static final int FLD_INVOICE_ID = 6;
    public static final int FLD_ID_PERKIRAAN_HPP = 7;
    public static final int FLD_ID_PERKIRAAN_HUTANG = 8;
    public static final int FLD_REFERENCE = 9;
    public static final int FLD_CONTACT_ID = 10;
    public static final int FLD_INVOICE_DETAIL_ID = 11;
    
    public static String[] fieldNames = {
        "COSTING_ID",
        "DESCRIPTION",
        "NUMBER",
        "UNIT_PRICE",
        "DISCOUNT",
        "STATUS",
        "INVOICE_ID",
        "ID_PERKIRAAN_HPP",
        "ID_PERKIRAAN_HUTANG",
        "REFERENCE",
        "CONTACT_ID",
        "INVOICE_DETAIL_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID,
        TYPE_STRING,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG
    };
    
    /** Creates a new instance of PstCosting */
    public PstCosting() {
    }
    
    public PstCosting(int i) throws DBException {
        super(new PstCosting());
    }
    
     public PstCosting(String sOid) throws DBException {
        super(new PstCosting(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
     
     public PstCosting(long lOid) throws DBException {
        super(new PstCosting(0));
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
        return deleteExc((Costing)entity);
    }
    
    public long fetchExc(Entity entity) throws Exception {
        Costing objCosting = PstCosting.fetchExc(entity.getOID());
        entity = (Entity)objCosting;
        return objCosting.getOID();
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
        return new PstCosting().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_COSTING;
    }
    
    public long insertExc(Entity entity) throws Exception {
        return PstCosting.insertExc((Costing)entity);
    }
    
    public long updateExc(Entity entity) throws Exception {
        return PstCosting.updateExc((Costing)entity);
    }
    
    public static Costing fetchExc(long lOid) throws DBException{
        try{
            Costing objCosting = new Costing();
            PstCosting pstCosting = new PstCosting(lOid);
            objCosting.setOID(lOid);
            
            objCosting.setContactId(pstCosting.getlong(FLD_CONTACT_ID));
            objCosting.setDescription(pstCosting.getString(FLD_DESCRIPTION));
            objCosting.setDiscount(pstCosting.getdouble(FLD_DISCOUNT));
            objCosting.setIdPerkiraanHpp(pstCosting.getlong(FLD_ID_PERKIRAAN_HPP));
            objCosting.setIdPerkiraanHutang(pstCosting.getlong(FLD_ID_PERKIRAAN_HUTANG));
            objCosting.setInvoiceId(pstCosting.getlong(FLD_INVOICE_ID));
            objCosting.setNumber(pstCosting.getInt(FLD_NUMBER));
            objCosting.setReference(pstCosting.getString(FLD_REFERENCE));
            objCosting.setStatus(pstCosting.getInt(FLD_STATUS));
            objCosting.setUnitPrice(pstCosting.getdouble(FLD_UNIT_PRICE));
            objCosting.setInvoiceDetailId(pstCosting.getlong(FLD_INVOICE_DETAIL_ID));
            
            return objCosting;            
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCosting(0), DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(Costing objCosting) throws DBException{
        try{
            PstCosting pstCosting = new PstCosting(0);
            
            pstCosting.setLong(FLD_CONTACT_ID, objCosting.getContactId());
            pstCosting.setString(FLD_DESCRIPTION, objCosting.getDescription());
            pstCosting.setDouble(FLD_DISCOUNT, objCosting.getDiscount());
            pstCosting.setLong(FLD_ID_PERKIRAAN_HPP, objCosting.getIdPerkiraanHpp());
            pstCosting.setLong(FLD_ID_PERKIRAAN_HUTANG, objCosting.getIdPerkiraanHutang());
            pstCosting.setLong(FLD_INVOICE_ID, objCosting.getInvoiceId());
            pstCosting.setInt(FLD_NUMBER, objCosting.getNumber());
            pstCosting.setString(FLD_REFERENCE, objCosting.getReference());
            pstCosting.setInt(FLD_STATUS, objCosting.getStatus());
            pstCosting.setDouble(FLD_UNIT_PRICE, objCosting.getUnitPrice());
            pstCosting.setLong(FLD_INVOICE_DETAIL_ID, objCosting.getInvoiceDetailId());
            
            pstCosting.insert();
            objCosting.setOID(pstCosting.getlong(FLD_COSTING_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCosting(0), DBException.UNKNOWN);
        }
        return objCosting.getOID();
    }
    
    public static long updateExc(Costing objCosting) throws DBException{
        try{
            if(objCosting.getOID() != 0){
                PstCosting pstCosting = new PstCosting(objCosting.getOID());
                
                pstCosting.setLong(FLD_CONTACT_ID, objCosting.getContactId());
                pstCosting.setString(FLD_DESCRIPTION, objCosting.getDescription());
                pstCosting.setDouble(FLD_DISCOUNT, objCosting.getDiscount());
                pstCosting.setLong(FLD_ID_PERKIRAAN_HPP, objCosting.getIdPerkiraanHpp());
                pstCosting.setLong(FLD_ID_PERKIRAAN_HUTANG, objCosting.getIdPerkiraanHutang());
                pstCosting.setLong(FLD_INVOICE_ID, objCosting.getInvoiceId());
                pstCosting.setInt(FLD_NUMBER, objCosting.getNumber());
                pstCosting.setString(FLD_REFERENCE, objCosting.getReference());
                pstCosting.setInt(FLD_STATUS, objCosting.getStatus());
                pstCosting.setDouble(FLD_UNIT_PRICE, objCosting.getUnitPrice());
                pstCosting.setLong(FLD_INVOICE_DETAIL_ID, objCosting.getInvoiceDetailId());
            
                pstCosting.update();
                return objCosting.getOID();
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCosting(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long lOid) throws DBException{
        try{
            PstCosting pstCosting = new PstCosting(lOid);
            pstCosting.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstCosting(0), DBException.UNKNOWN);
        }
        return lOid;
    }
    
    public static Vector list(int iStart, int iRecordToGet, String strWhClause, String strOrderBy){
        DBResultSet dbrs = null;
        Vector vResult = new Vector(1,1);
            try{
                String sql = " SELECT * FROM "+TBL_COSTING;
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
                System.out.println(sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();                
                while(rs.next()){                    
                    Costing objCosting = new Costing();
                    objCosting = resultToObject(rs, objCosting);
                    vResult.add(objCosting);
                    System.out.println("vResult.size() :::: "+vResult.size());
                }
                rs.close();
                return vResult;                         
            }catch(Exception e){
                System.out.println("Exception on "+new PstCosting().getClass().getName()+".list() ::: "+e.toString());
            }
        return new Vector(1,1);
    }
    
    public static Costing resultToObject(ResultSet rs, Costing objCosting){
        try{
            objCosting.setOID(rs.getLong(fieldNames[FLD_COSTING_ID]));
            objCosting.setContactId(rs.getLong(fieldNames[FLD_CONTACT_ID]));
            objCosting.setDescription(rs.getString(fieldNames[FLD_DESCRIPTION]));
            objCosting.setDiscount(rs.getDouble(fieldNames[FLD_DISCOUNT]));
            objCosting.setIdPerkiraanHpp(rs.getLong(fieldNames[FLD_ID_PERKIRAAN_HPP]));
            objCosting.setIdPerkiraanHutang(rs.getLong(fieldNames[FLD_ID_PERKIRAAN_HUTANG]));
            objCosting.setInvoiceId(rs.getLong(fieldNames[FLD_INVOICE_ID]));
            objCosting.setNumber(rs.getInt(fieldNames[FLD_NUMBER]));
            objCosting.setReference(rs.getString(fieldNames[FLD_REFERENCE]));
            objCosting.setStatus(rs.getInt(fieldNames[FLD_STATUS]));
            objCosting.setUnitPrice(rs.getDouble(fieldNames[FLD_UNIT_PRICE]));
            objCosting.setInvoiceDetailId(rs.getLong(fieldNames[FLD_INVOICE_DETAIL_ID]));
        }catch(Exception e){
            System.out.println("Exception on "+new PstCosting().getClass().getName()+".resultToObject() ::: "+e.toString());
        }
        return objCosting;
    }
    
    public static int getCount(String strWhClause){
        DBResultSet dbrs = null;
        int iResult = 0;
            try{
                String sql = " SELECT COUNT("+fieldNames[FLD_COSTING_ID]+") FROM "+TBL_COSTING;
                if(strWhClause != null && strWhClause.length() > 0)
                    sql += " WHERE "+strWhClause;
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    iResult = rs.getInt(1);
                }
            }catch(Exception e){
                iResult = 0;
                System.out.println("Exception on "+new PstCosting().getClass().getName()+".getCount() ::: "+e.toString());
            }
        return iResult;
    }
    
    public static synchronized long getIdCosting(long invDetailId){
	DBResultSet dbrs = null;
	long lResult = 0;
	try{
	    String sql = " SELECT "+fieldNames[PstCosting.FLD_COSTING_ID]+
			 " FROM "+TBL_COSTING+" WHERE "+fieldNames[PstCosting.FLD_INVOICE_DETAIL_ID]+
			 " = "+invDetailId;
	    
	    System.out.println("SQL PstCosting.getIdCosting :::::: "+sql);
	    dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
	    while(rs.next()){
		lResult = rs.getLong(fieldNames[PstCosting.FLD_COSTING_ID]);
	    }
	}catch(Exception e){}
	return lResult;
    }
    
    public static synchronized long getSupplierId(Costing objCosting, long supplierId){
	long lResult = 0;
	try{
	    if(objCosting.getContactId() == supplierId || supplierId == 0){
		lResult = objCosting.getContactId();
	    }else{
		lResult = supplierId;
	    }
	}catch(Exception e){}
	return lResult;
    }
    
    public static synchronized String getDescription(Costing objCosting, String description){
	String sResult = null;
	try{
	    if(objCosting.getDescription().equalsIgnoreCase(description)){
		    sResult = objCosting.getDescription();
	    }else{
		    sResult = description;
	    }
	}catch(Exception e){}
	return sResult;
    }
    
    public static synchronized long getIdHpp(Costing objCosting, long idHpp){
	long lResult = 0;
	try{
	    if(objCosting.getIdPerkiraanHpp() == idHpp || idHpp == 0){
		lResult = objCosting.getIdPerkiraanHpp();
	    }else{
		lResult = idHpp;
	    }
	}catch(Exception e){}
	return lResult;
    }
    
    public static synchronized long getIdPayable(Costing objCosting, long idPayable){
	long lResult = 0;
	try{
	    if(objCosting.getIdPerkiraanHpp() == idPayable || idPayable == 0){
		lResult = objCosting.getIdPerkiraanHpp();
	    }else{
		lResult = idPayable;
	    }
	}catch(Exception e){}
	return lResult;
    }
    
    public static synchronized double getUnitPrice(Costing objCosting, double netPrice){
	double dResult = 0;
	try{
	    if(objCosting.getUnitPrice() == netPrice || netPrice == 0){
		dResult = objCosting.getUnitPrice();
	    }else{
		dResult = netPrice;
	    }
	}catch(Exception e){}
	return dResult;
    }
    
    public static void main(String[] arg){
        PstCosting pstCosting = new PstCosting();
        Costing objCosting = new Costing();
        try{
            objCosting = pstCosting.fetchExc(504404354507222567L);
        }catch(Exception e){}
        objCosting.setStatus(7);
        
        try{
            long lOid = PstCosting.updateExc(objCosting);
            System.out.println("SUKSES "+objCosting.getInvoiceId());
        }catch(Exception e){
            System.out.println("GAGAL ::: "+e.toString());
        }
    }
}

