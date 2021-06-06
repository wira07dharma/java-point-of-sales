/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.region;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_Persintent;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dw1p4
 */
public class PstWard extends DBHandler implements I_DBInterface, I_DBType, I_Persintent{
    public static final String TBL_WARD="aiso_ward";
    
    public static final int FLD_WARD_ID=0;
    public static final int FLD_WARD_NAME=1;
    
    //UPDATE tanggal 7 Maret 2013 oleh Hadi sehubungan penambahan field database
    public static final int FLD_SUBREGENCY_ID = 2;
    
    public static final String [] fieldNames={
        "WARD_ID",
        "WARD_NAME",
        
        //update tanggal 7 oleh Hadi Putra
        "SUBREGENCY_ID"
    };
    
    public static final int [] fieldTypes={
        TYPE_PK+TYPE_ID+TYPE_LONG,
        TYPE_STRING,
        
        //update tanggal 7 Maret 2013 oleh Hadi Putra
        TYPE_LONG
    };
    
    public PstWard(){
    }
    
    public PstWard(int i) throws DBException {
        super(new PstWard());
    }
    
    
    public PstWard(String sOid) throws DBException {
        super(new PstWard(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstWard(long lOid) throws DBException {
        super(new PstWard(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
        
    }
    
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getTableName() {
        return TBL_WARD;
    }
    
    public String getPersistentName() {
        return new PstWard().getClass().getName();
    }
    
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    
    public long delete(Entity ent) {
        return deleteExc(ent.getOID());
    }
    
    public long insert(Entity ent){
        try{
            return PstWard.insert((Ward) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    
    public long update(Entity ent) {
        return update((Ward) ent);
    }
    
    public long fetch(Entity ent) {
        Ward entObj = PstWard.fetch(ent.getOID());
        ent = (Entity)entObj;
        return ent.getOID();
    }
    
    public static Ward fetch(long oid) {
        Ward entObj = new Ward();
        try {
            PstWard pstObj = new PstWard(oid);
            entObj.setOID(oid);
            entObj.setWardName(pstObj.getString(FLD_WARD_NAME));
            
            entObj.setIdSubRegency(pstObj.getlong(FLD_SUBREGENCY_ID));
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }
    
    /**
     * 
     * @param entObj
     * @return
     * @throws DBException 
     */
    public static long insert(Ward entObj) throws DBException  {
     try{
        PstWard pstObj = new PstWard(0);
        
        pstObj.setString(FLD_WARD_NAME, entObj.getWardName());
        
        pstObj.setLong(FLD_SUBREGENCY_ID, entObj.getIdSubRegency());
        
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_WARD_ID));
        return entObj.getOID();
     }catch(DBException e) {
        throw  e;
     }  
    }
    
    public static long update(Ward entObj) {
        /**
         * update data pkl
         */
            try {
                PstWard pstObj = new PstWard(entObj.getOID());
                
                pstObj.setString(FLD_WARD_NAME, entObj.getWardName());
                
                pstObj.setLong(FLD_SUBREGENCY_ID, entObj.getIdSubRegency());

                pstObj.update();
                return entObj.getOID();
            } catch (Exception e) {
                System.out.println(e);
            }
        return 0;
    }
    
    public static long deleteExc(long oid) {
        /**
         * delete data pkl
         */
        try {
            PstWard pstObj = new PstWard(oid);
            pstObj.delete();
            return oid;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    /**
     * 
     * @param whereClause
     * @return 
     */
    public static int getCount(String whereClause){
        DBResultSet dbrs=null;
        try{
            int count = 0;
            String sql = " SELECT COUNT("+fieldNames[FLD_WARD_ID] +") AS NRCOUNT" +
            " FROM " + TBL_WARD;
            
            
            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            
            //System.out.println(sql);
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        }
        catch (Exception exc){
            System.out.println("getCount "+ exc);
            return 0;
        }
        finally{
            DBResultSet.close(dbrs);
        }        
        
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_WARD + " ";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Ward ward = new Ward();
                resultToObject(rs, ward);
                lists.add(ward);
            }
            rs.close();
            return lists;
        } catch (Exception e) {          
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    private static void resultToObject(ResultSet rs, Ward entObj) {
        try {
            entObj.setOID(rs.getLong(fieldNames[FLD_WARD_ID]));
            entObj.setWardName(rs.getString(fieldNames[FLD_WARD_NAME]));
            
            //update tanggal 7 Maret 2013 oleh Hadi
            entObj.setIdSubRegency(rs.getLong(fieldNames[FLD_SUBREGENCY_ID]));
            
        }catch(Exception e){
            System.out.println("resultToObject() appuser -> : " + e.toString());
        }
    }
    
}
