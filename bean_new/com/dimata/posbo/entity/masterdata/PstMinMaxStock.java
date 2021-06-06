package com.dimata.posbo.entity.masterdata;

/* package java */
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

import com.dimata.common.entity.location.*;
import org.json.JSONObject;

public class PstMinMaxStock extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    //public static final  String TBL_MINMAXSTOCK = "POS_MINMAXSTOCK";
    public static final  String TBL_MINMAXSTOCK = "pos_minmaxstock";
    
    public static final  int FLD_MINMAXSTOCK_ID = 0;
    public static final  int FLD_MATERIAL_ID    = 1;
    public static final  int FLD_LOCATION_ID    = 2;
    public static final  int FLD_QTY_MIN        = 3;
    public static final  int FLD_QTY_MAX        = 4;
    
    public static final  String[] fieldNames = {
        "MINMAXSTOCK_ID",
        "MATERIAL_ID",
        "LOCATION_ID",
        "QTY_MIN",
        "QTY_MAX"
    };
    
    public static final  int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    
    public PstMinMaxStock(){
    }
    
    public PstMinMaxStock(int i) throws DBException {
        super(new PstMinMaxStock());
    }
    
    public PstMinMaxStock(String sOid) throws DBException {
        super(new PstMinMaxStock(0));
        if(!locate(sOid))
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstMinMaxStock(long lOid) throws DBException {
        super(new PstMinMaxStock(0));
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
        return TBL_MINMAXSTOCK;
    }
    
    public String[] getFieldNames(){
        return fieldNames;
    }
    
    public int[] getFieldTypes(){
        return fieldTypes;
    }
    
    public String getPersistentName(){
        return new PstMinMaxStock().getClass().getName();
    }
    
    public long fetchExc(Entity ent) throws Exception{
        MinMaxStock minMaxStock = fetchExc(ent.getOID());
        ent = (Entity)minMaxStock;
        return minMaxStock.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{
        return insertExc((MinMaxStock) ent);
    }
    
    public long updateExc(Entity ent) throws Exception{
        return updateExc((MinMaxStock) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception{
        if(ent==null){
            throw new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static MinMaxStock fetchExc(long oid) throws DBException {
        try {
            MinMaxStock minMaxStock = new MinMaxStock();
            PstMinMaxStock pstMinMaxStock = new PstMinMaxStock(oid);
            minMaxStock.setOID(oid);
            
            minMaxStock.setMaterialId(pstMinMaxStock.getlong(FLD_MATERIAL_ID));
            minMaxStock.setLocationId(pstMinMaxStock.getlong(FLD_LOCATION_ID));
            minMaxStock.setQtyMin(pstMinMaxStock.getdouble(FLD_QTY_MIN));
            minMaxStock.setQtyMax(pstMinMaxStock.getdouble(FLD_QTY_MAX));
            
            return minMaxStock;
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstMinMaxStock(0),DBException.UNKNOWN);
        }
    }
    
    public static long insertExc(MinMaxStock minMaxStock) throws DBException {
        try {
            PstMinMaxStock pstMinMaxStock = new PstMinMaxStock(0);
            
            pstMinMaxStock.setLong(FLD_MATERIAL_ID, minMaxStock.getMaterialId());
            pstMinMaxStock.setLong(FLD_LOCATION_ID, minMaxStock.getLocationId());
            pstMinMaxStock.setDouble(FLD_QTY_MIN, minMaxStock.getQtyMin());
            pstMinMaxStock.setDouble(FLD_QTY_MAX, minMaxStock.getQtyMax());
            
            pstMinMaxStock.insert();
            minMaxStock.setOID(pstMinMaxStock.getlong(FLD_MINMAXSTOCK_ID));
            //Update max dan min stock utk current periode di lokasi ini
            boolean hasil = updateMinMaxMaterialStock(minMaxStock.getMaterialId()
            ,minMaxStock.getLocationId()
            ,minMaxStock.getQtyMin()
            ,minMaxStock.getQtyMax());
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstMinMaxStock(0),DBException.UNKNOWN);
        }
        return minMaxStock.getOID();
    }
    
    public static long updateExc(MinMaxStock minMaxStock) throws DBException {
        try {
            if(minMaxStock.getOID() != 0) {
                PstMinMaxStock pstMinMaxStock = new PstMinMaxStock(minMaxStock.getOID());
                
                pstMinMaxStock.setLong(FLD_MATERIAL_ID, minMaxStock.getMaterialId());
                pstMinMaxStock.setLong(FLD_LOCATION_ID, minMaxStock.getLocationId());
                pstMinMaxStock.setDouble(FLD_QTY_MIN, minMaxStock.getQtyMin());
                pstMinMaxStock.setDouble(FLD_QTY_MAX, minMaxStock.getQtyMax());
                
                pstMinMaxStock.update();
                //Update max dan min stock utk current periode di lokasi ini
                boolean hasil = updateMinMaxMaterialStock(minMaxStock.getMaterialId()
                ,minMaxStock.getLocationId()
                ,minMaxStock.getQtyMin()
                ,minMaxStock.getQtyMax());
                return minMaxStock.getOID();
            }
        }
        catch(DBException dbe) {
            throw dbe;
        }
        catch(Exception e) {
            throw new DBException(new PstMinMaxStock(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {
        try {
            PstMinMaxStock pstMinMaxStock = new PstMinMaxStock(oid);
            pstMinMaxStock.delete();
        }
        catch(DBException dbe) {
            System.out.println("DBE Exception : " + dbe);
            throw dbe;
        }
        catch(Exception e) {
            System.out.println("Exception : " + e);
            throw new DBException(new PstMinMaxStock(0),DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static void deleteByMaterial(long oidMaterial) {
        DBResultSet dbrs = null;
        try {
            String sql = "DELETE FROM " + PstMinMaxStock.TBL_MINMAXSTOCK +
            " WHERE " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MATERIAL_ID] +
            " = " + oidMaterial;
            DBHandler.execUpdate(sql);
        }
        catch(Exception e) {
            System.out.println("PstMinMaxStock.deleteByMaterial() err : "+e.toString());
        }
        finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static Vector listAll(){
        return list(0, 500, "","");
    }
    
    public static Vector list(int limitStart,int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MM." + fieldNames[FLD_MINMAXSTOCK_ID] +
            ", MM." + fieldNames[FLD_MATERIAL_ID] +
            ", MM." + fieldNames[FLD_LOCATION_ID] +
            ", MM." + fieldNames[FLD_QTY_MIN] +
            ", MM." + fieldNames[FLD_QTY_MAX] +
            ", LOC." + PstUnit.fieldNames[PstUnit.FLD_NAME] +
            " FROM " + TBL_MINMAXSTOCK + " MM" +
            " INNER JOIN " + PstLocation.TBL_P2_LOCATION + " LOC" +
            " ON MM." + fieldNames[FLD_LOCATION_ID] +
            " = LOC." + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
            
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
                    ;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                Vector temp = new Vector();
                MinMaxStock minMaxStock = new MinMaxStock();
                Location loc = new Location();
                
                minMaxStock.setOID(rs.getLong(1));
                minMaxStock.setMaterialId(rs.getLong(2));
                minMaxStock.setLocationId(rs.getLong(3));
                minMaxStock.setQtyMin(rs.getDouble(4));
                minMaxStock.setQtyMax(rs.getDouble(5));
                temp.add(minMaxStock);
                
                loc.setName(rs.getString(6));
                temp.add(loc);
                
                lists.add(temp);
            }
            rs.close();
            return lists;
            
        }
        catch(Exception e) {
            System.out.println(e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static void resultToObject(ResultSet rs, MinMaxStock minMaxStock) {
        try {
            minMaxStock.setOID(rs.getLong(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MINMAXSTOCK_ID]));
            minMaxStock.setMaterialId(rs.getLong(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MATERIAL_ID]));
            minMaxStock.setLocationId(rs.getLong(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_LOCATION_ID]));
            minMaxStock.setQtyMin(rs.getDouble(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MIN]));
            minMaxStock.setQtyMax(rs.getDouble(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MAX]));
            
        }catch(Exception e){ }
    }
    
    public static boolean checkOID(long minMaxStockId){
        DBResultSet dbrs = null;
        boolean result = false;
        try{
            String sql = "SELECT * FROM " + TBL_MINMAXSTOCK + " WHERE " +
            fieldNames[PstMinMaxStock.FLD_MINMAXSTOCK_ID] +
            " = " + minMaxStockId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                result = true;
            }
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
            String sql = "SELECT COUNT(MM."+ fieldNames[PstMinMaxStock.FLD_MINMAXSTOCK_ID] + ") FROM " +
            TBL_MINMAXSTOCK + " MM";
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            int count = 0;
            while(rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        }catch(Exception e) {
            return 0;
        }finally {
            DBResultSet.close(dbrs);
        }
    }
    
    private static boolean updateMinMaxMaterialStock(long oidMaterial, long oidLocation
    , double qtyMin, double qtyMax) {
        boolean result = false;
        try {
            long oidMaterialStock = PstMaterialStock.fetchByMaterialLocation(oidMaterial,oidLocation);
            MaterialStock matstock = PstMaterialStock.fetchExc(oidMaterialStock);
            matstock.setQtyMin(qtyMin);
            matstock.setQtyMax(qtyMax);
            long oidMatStock = PstMaterialStock.updateExc(matstock);
            result = true;
        }
        catch(Exception e) {
            System.out.println("err : "+e.toString());
        }
        return result;
    }
     public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MinMaxStock minMaxStock = PstMinMaxStock.fetchExc(oid);
                object.put(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MINMAXSTOCK_ID], minMaxStock.getOID());
                object.put(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_LOCATION_ID], minMaxStock.getLocationId());
                object.put(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MATERIAL_ID], minMaxStock.getMaterialId());
                object.put(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MAX], minMaxStock.getQtyMax());
                object.put(PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MIN], minMaxStock.getQtyMin());
            }catch(Exception exc){}

            return object;
        }   
}
