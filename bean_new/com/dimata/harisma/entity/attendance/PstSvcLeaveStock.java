/*
 * PstSvcLeaveStock.java
 *
 * Created on February 24, 2004, 2:06 PM
 */

package com.dimata.harisma.entity.attendance;

/**
 *
 * @author  sutaya
 */

/* package java */
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

public class PstSvcLeaveStock extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language{
    
    public static final String TBL_HR_SVC_LEAVE_STOCK = "HR_SVC_LEAVE_STOCK";
    
    public static final int FLD_SVC_LEAVE_STOCK_ID = 0;
    public static final int FLD_LEAVE_STOCK_ID = 1;
    public static final int FLD_PERIODE = 2;
    public static final int FLD_ADD_AL = 3;
    public static final int FLD_ADD_LL = 4;
    public static final int FLD_LOS = 5;
    
    public static final String[] fieldNames = {
        "SVC_LEAVE_STOCK_ID",
        "LEAVE_STOCK_ID",
        "PERIODE",
        "ADD_AL",
        "ADD_LL",
        "LOS"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };
    
    
    /** Creates a new instance of PstSvcLeaveStock */
    public PstSvcLeaveStock() {
    }
    
    public PstSvcLeaveStock(int i)throws DBException{
        super(new PstSvcLeaveStock());
    }
    public PstSvcLeaveStock(String sOid) throws DBException {
        super(new PstSvcLeaveStock(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstSvcLeaveStock(long lOid) throws DBException {
        super(new PstSvcLeaveStock(0));
        String sOid = "0";
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
        return TBL_HR_SVC_LEAVE_STOCK;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstSvcLeaveStock().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        SvcLeaveStock svcLeavestock = fetchExc(ent.getOID());
        ent = (Entity)svcLeavestock;
        return svcLeavestock.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((SvcLeaveStock) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((SvcLeaveStock) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static SvcLeaveStock fetchExc(long oid) throws DBException{
        try{
            SvcLeaveStock svcLeaveStock = new SvcLeaveStock();
            PstSvcLeaveStock pstSvcLeaveStock = new PstSvcLeaveStock(oid);
            svcLeaveStock.setOID(oid);
            
            svcLeaveStock.setLeaveStockId(pstSvcLeaveStock.getlong(FLD_LEAVE_STOCK_ID));
            svcLeaveStock.setPeriode(pstSvcLeaveStock.getDate(FLD_PERIODE));
            svcLeaveStock.setAlAmount(pstSvcLeaveStock.getInt(FLD_ADD_AL));
            svcLeaveStock.setLlAmount(pstSvcLeaveStock.getInt(FLD_ADD_LL));
            svcLeaveStock.setLoss(pstSvcLeaveStock.getInt(FLD_LOS));
            
            return svcLeaveStock;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSvcLeaveStock(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(SvcLeaveStock svcLeaveStock) throws DBException{
        try{
            PstSvcLeaveStock pstSvcLeaveStock = new PstSvcLeaveStock(0);
            
            pstSvcLeaveStock.setLong(FLD_LEAVE_STOCK_ID, svcLeaveStock.getLeaveStockId());
            pstSvcLeaveStock.setDate(FLD_PERIODE, svcLeaveStock.getPeriode());
            pstSvcLeaveStock.setInt(FLD_ADD_AL, svcLeaveStock.getAlAmount());
            pstSvcLeaveStock.setInt(FLD_ADD_LL, svcLeaveStock.getLlAmount());
            pstSvcLeaveStock.setInt(FLD_LOS, svcLeaveStock.getLoss());
            
            pstSvcLeaveStock.insert();
            svcLeaveStock.setOID(pstSvcLeaveStock.getlong(FLD_SVC_LEAVE_STOCK_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSvcLeaveStock(0),DBException.UNKNOWN);
        }
        return svcLeaveStock.getOID();
    }
    
    public static long updateExc(SvcLeaveStock svcLeaveStock) throws DBException{
        try{
            if(svcLeaveStock.getOID() != 0){
                PstSvcLeaveStock pstSvcLeaveStock = new PstSvcLeaveStock(svcLeaveStock.getOID());
                
                pstSvcLeaveStock.setLong(FLD_LEAVE_STOCK_ID, svcLeaveStock.getLeaveStockId());
                pstSvcLeaveStock.setDate(FLD_PERIODE, svcLeaveStock.getPeriode());
                pstSvcLeaveStock.setInt(FLD_ADD_AL, svcLeaveStock.getAlAmount());
                pstSvcLeaveStock.setInt(FLD_ADD_LL, svcLeaveStock.getLlAmount());
                pstSvcLeaveStock.setInt(FLD_LOS, svcLeaveStock.getLoss());
                
                pstSvcLeaveStock.update();
                return svcLeaveStock.getOID();
                
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSvcLeaveStock(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException{
        try{
            PstSvcLeaveStock pstSvcLeaveStock = new PstSvcLeaveStock(oid);
            pstSvcLeaveStock.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstSvcLeaveStock(0),DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_HR_SVC_LEAVE_STOCK;
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if(limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                SvcLeaveStock svcLeaveStock = new SvcLeaveStock();
                resultToObject(rs, svcLeaveStock);
                lists.add(svcLeaveStock);
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
    
    public static void resultToObject(ResultSet rs, SvcLeaveStock svcLeaveStock){
        try{
            svcLeaveStock.setOID(rs.getLong(PstSvcLeaveStock.fieldNames[PstSvcLeaveStock.FLD_SVC_LEAVE_STOCK_ID]));
            svcLeaveStock.setLeaveStockId(rs.getLong(PstSvcLeaveStock.fieldNames[PstSvcLeaveStock.FLD_LEAVE_STOCK_ID]));
            svcLeaveStock.setPeriode(rs.getDate(PstSvcLeaveStock.fieldNames[PstSvcLeaveStock.FLD_PERIODE]));
            svcLeaveStock.setAlAmount(rs.getInt(PstSvcLeaveStock.fieldNames[PstSvcLeaveStock.FLD_ADD_AL]));
            svcLeaveStock.setLlAmount(rs.getInt(PstSvcLeaveStock.fieldNames[PstSvcLeaveStock.FLD_ADD_LL]));
            svcLeaveStock.setLoss(rs.getInt(PstSvcLeaveStock.fieldNames[PstSvcLeaveStock.FLD_LOS]));
                        
        }catch(Exception e){ }
    }
    
    public static boolean checkOID(long svcLeaveStockId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_SVC_LEAVE_STOCK + " WHERE " + 
						PstSvcLeaveStock.fieldNames[PstSvcLeaveStock.FLD_SVC_LEAVE_STOCK_ID] + " = " + svcLeaveStockId;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}
    
    public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstSvcLeaveStock.fieldNames[PstSvcLeaveStock.FLD_SVC_LEAVE_STOCK_ID] + ") FROM " + TBL_HR_SVC_LEAVE_STOCK;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			int count = 0;
			while(rs.next()) { count = rs.getInt(1); }

			rs.close();
			return count;
		}catch(Exception e) {
			return 0;
		}finally {
			DBResultSet.close(dbrs);
		}
	}
}
