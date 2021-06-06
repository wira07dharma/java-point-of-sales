/*
 * PstOtherCost.java
 *
 * Created on August 3th, 2005
 * by wpulantara
 */

package com.dimata.pos.entity.billing;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

/* package java */
import java.sql.ResultSet;
import java.util.Vector;

/* package qdep */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;

public class PstOtherCost extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final  String TBL_CASH_OTHER_COST = "cash_other_cost";
    
    public static final  int FLD_CASH_OTHER_COST_ID = 0;
    public static final  int FLD_CASH_BILL_MAIN_ID = 1;
    public static final  int FLD_NAME = 2;
    public static final  int FLD_DESCRIPTION = 3;
    public static final  int FLD_CURRENCY_ID = 4;
    public static final  int FLD_RATE = 5;
    public static final  int FLD_AMOUNT = 6;
    
    public static final  String[] fieldNames = {
        "CASH_OTHER_COST_ID",
        "CASH_BILL_MAIN_ID",
        "NAME",
        "DESCRIPTION",
        "CURRENCY_ID",
        "RATE",
        "AMOUNT"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public static final int UPDATE_STATUS_NONE = 0;
    public static final int UPDATE_STATUS_INSERTED = 1;
    public static final int UPDATE_STATUS_UPDATED = 2;
    public static final int UPDATE_STATUS_DELETED = 3;
    
    public PstOtherCost(){
    }
    
    public PstOtherCost(int i) throws DBException {
        super(new PstOtherCost());
    }
    
    public PstOtherCost(String sOid) throws DBException {
        super(new PstOtherCost(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstOtherCost(long lOid) throws DBException {
        super(new PstOtherCost(0));
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
        return TBL_CASH_OTHER_COST;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstOtherCost().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        OtherCost otherCost = fetchExc(ent.getOID());
        ent = (Entity)otherCost;
        return otherCost.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((OtherCost) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((OtherCost) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static OtherCost fetchExc(long oid) throws DBException{
        try{
            OtherCost otherCost = new OtherCost();
            PstOtherCost pstOtherCost = new PstOtherCost(oid);
            otherCost.setOID(oid);
            
            otherCost.setBillMainId(pstOtherCost.getlong(FLD_CASH_BILL_MAIN_ID));
            otherCost.setName(pstOtherCost.getString(FLD_NAME));
            otherCost.setDescription(pstOtherCost.getString(FLD_DESCRIPTION));
            otherCost.setCurrencyId(pstOtherCost.getlong(FLD_CURRENCY_ID));
            otherCost.setRate(pstOtherCost.getdouble(FLD_RATE));
            otherCost.setAmount(pstOtherCost.getdouble(FLD_AMOUNT));
            
            return otherCost;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstOtherCost(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(OtherCost otherCost) throws DBException{
        try{
            PstOtherCost pstOtherCost = new PstOtherCost(0);
            
            pstOtherCost.setLong(FLD_CASH_BILL_MAIN_ID, otherCost.getBillMainId());
            pstOtherCost.setString(FLD_NAME, otherCost.getName());
            pstOtherCost.setString(FLD_DESCRIPTION, otherCost.getDescription());
            pstOtherCost.setLong(FLD_CURRENCY_ID, otherCost.getCurrencyId());
            pstOtherCost.setDouble(FLD_RATE, otherCost.getRate());
            pstOtherCost.setDouble(FLD_AMOUNT, otherCost.getAmount());
            
            pstOtherCost.insert();
            otherCost.setOID(pstOtherCost.getlong(FLD_CASH_OTHER_COST_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstOtherCost(0),DBException.UNKNOWN);
        }
        return otherCost.getOID();
    }

    public static long insertExcByOid(OtherCost otherCost) throws DBException{
        try{
            PstOtherCost pstOtherCost = new PstOtherCost(0);

            pstOtherCost.setLong(FLD_CASH_BILL_MAIN_ID, otherCost.getBillMainId());
            pstOtherCost.setString(FLD_NAME, otherCost.getName());
            pstOtherCost.setString(FLD_DESCRIPTION, otherCost.getDescription());
            pstOtherCost.setLong(FLD_CURRENCY_ID, otherCost.getCurrencyId());
            pstOtherCost.setDouble(FLD_RATE, otherCost.getRate());
            pstOtherCost.setDouble(FLD_AMOUNT, otherCost.getAmount());

            pstOtherCost.insertByOid(TYPE_ID);
            //otherCost.setOID(pstOtherCost.getlong(FLD_CASH_OTHER_COST_ID));
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstOtherCost(0),DBException.UNKNOWN);
        }
        return otherCost.getOID();
    }


    public static long updateExc(OtherCost otherCost) throws DBException{
        try{
            if(otherCost.getOID() != 0){
                PstOtherCost pstOtherCost = new PstOtherCost(otherCost.getOID());
                
                pstOtherCost.setLong(FLD_CASH_BILL_MAIN_ID, otherCost.getBillMainId());
                pstOtherCost.setString(FLD_NAME, otherCost.getName());
                pstOtherCost.setString(FLD_DESCRIPTION, otherCost.getDescription());
                pstOtherCost.setLong(FLD_CURRENCY_ID, otherCost.getCurrencyId());
                pstOtherCost.setDouble(FLD_RATE, otherCost.getRate());
                pstOtherCost.setDouble(FLD_AMOUNT, otherCost.getAmount());
                
                pstOtherCost.update();
                return otherCost.getOID();
                
            }
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstOtherCost(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException{
        try{
            PstOtherCost pstOtherCost = new PstOtherCost(oid);
            pstOtherCost.delete();
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstOtherCost(0),DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_CASH_OTHER_COST;
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
                OtherCost otherCost = new OtherCost();
                resultToObject(rs, otherCost);
                lists.add(otherCost);
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
    
    public static void resultToObject(ResultSet rs, OtherCost otherCost){
        try{
            otherCost.setOID(rs.getLong(PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_OTHER_COST_ID]));
            otherCost.setBillMainId(rs.getLong(PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_BILL_MAIN_ID]));
            otherCost.setName(rs.getString(PstOtherCost.fieldNames[PstOtherCost.FLD_NAME]));
            otherCost.setDescription(rs.getString(PstOtherCost.fieldNames[PstOtherCost.FLD_DESCRIPTION]));
            otherCost.setCurrencyId(rs.getLong(PstOtherCost.fieldNames[PstOtherCost.FLD_CURRENCY_ID]));
            otherCost.setRate(rs.getDouble(PstOtherCost.fieldNames[PstOtherCost.FLD_RATE]));
            otherCost.setAmount(rs.getDouble(PstOtherCost.fieldNames[PstOtherCost.FLD_AMOUNT]));
            
        }catch(Exception e){ }
    }
    
    public static boolean checkOID(long cashOtherCostId){
        DBResultSet dbrs = null;
        boolean result = false;
        try{
            String sql = "SELECT * FROM " + TBL_CASH_OTHER_COST + " WHERE " +
            PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_OTHER_COST_ID] + " = " + cashOtherCostId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) { result = true; }
            rs.close();
        }catch(Exception e){
            System.out.println("err : "+e.toString());
        }finally{
            DBResultSet.close(dbrs);
            
        }
        return result;
    }
    
    public static int getCount(String whereClause){
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT("+ PstOtherCost.fieldNames[PstOtherCost.FLD_CASH_OTHER_COST_ID] + ") FROM " + TBL_CASH_OTHER_COST;
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
    
    
    /* This method used to find current data */
    public static int findLimitStart( long oid, int recordToGet, String whereClause){
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found =false;
        for(int i=0; (i < size) && !found ; i=i+recordToGet){
            Vector list =  list(i,recordToGet, whereClause, order);
            start = i;
            if(list.size()>0){
                for(int ls=0;ls<list.size();ls++){
                    OtherCost otherCost = (OtherCost)list.get(ls);
                    if(oid == otherCost.getOID())
                        found=true;
                }
            }
        }
        if((start >= size) && (size > 0))
            start = start - recordToGet;
        
        return start;
    }
}
