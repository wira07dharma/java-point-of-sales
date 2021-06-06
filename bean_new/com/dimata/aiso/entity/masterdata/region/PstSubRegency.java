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
public class PstSubRegency extends DBHandler implements I_DBInterface, I_DBType, I_Persintent{
    public static final String TBL_SUBREGENCY="aiso_subregency";
    
    public static final int FLD_ADDR_SUBREGENCY_ID=0;
    public static final int FLD_SUBREGENCY_NAME=1;
    
    //Update Tanggl 7 Maret 2013 oleh Hadi
    public static final int FLD_REGENCY_ID = 2;
    
    public static final String [] fieldNames={
        "ADDR_SUBREGENCY_ID",
        "SUBREGENCY",
        
        //Update
        "REGENCY_ID"
    };
    
    public static final int [] fieldTypes={
        TYPE_PK+TYPE_ID+TYPE_LONG,
        TYPE_STRING,
        
        //Update
        TYPE_LONG
    };
    
    public PstSubRegency(){
    }
    
    public PstSubRegency(int i) throws DBException {
        super(new PstSubRegency());
    }
    
    
    public PstSubRegency(String sOid) throws DBException {
        super(new PstSubRegency(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstSubRegency(long lOid) throws DBException {
        super(new PstSubRegency(0));
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
        return TBL_SUBREGENCY;
    }
    
    public String getPersistentName() {
        return new PstSubRegency().getClass().getName();
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
            return PstSubRegency.insert((SubRegency) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    
    public long update(Entity ent) {
        return update((SubRegency) ent);
    }
    
    public long fetch(Entity ent) {
        SubRegency entObj = PstSubRegency.fetch(ent.getOID());
        ent = (Entity)entObj;
        return ent.getOID();
    }
    
    public static SubRegency fetch(long oid) {
        SubRegency entObj = new SubRegency();
        try {
            PstSubRegency pstObj = new PstSubRegency(oid);
            entObj.setOID(oid);
            entObj.setSubRegencyName(pstObj.getString(FLD_SUBREGENCY_NAME));
            
            //Update
            entObj.setIdRegency(pstObj.getlong(FLD_REGENCY_ID));
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
    public static long insert(SubRegency entObj) throws DBException  {
     try{
        PstSubRegency pstObj = new PstSubRegency(0);
        
        pstObj.setString(FLD_SUBREGENCY_NAME, entObj.getSubRegencyName());
        
        //Update
        pstObj.setLong(FLD_REGENCY_ID, entObj.getIdRegency());
        
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_ADDR_SUBREGENCY_ID));       
        return entObj.getOID();
     }catch(DBException e) {
        throw  e;
     }  
    }
    
    public static long update(SubRegency entObj) {
        /**
         * update data pkl
         */
            try {
                PstSubRegency pstObj = new PstSubRegency(entObj.getOID());
                
                pstObj.setString(FLD_SUBREGENCY_NAME, entObj.getSubRegencyName());
                
                //Update
                pstObj.setLong(FLD_REGENCY_ID, entObj.getIdRegency());

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
            PstSubRegency pstObj = new PstSubRegency(oid);
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
            String sql = " SELECT COUNT("+fieldNames[FLD_ADDR_SUBREGENCY_ID] +") AS NRCOUNT" +
            " FROM " + TBL_SUBREGENCY;
            
            
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
            String sql = "SELECT * FROM " + TBL_SUBREGENCY + " ";
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
                SubRegency subRegency = new SubRegency();
                resultToObject(rs, subRegency);
                lists.add(subRegency);
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
    
    private static void resultToObject(ResultSet rs, SubRegency entObj) {
        try {
            entObj.setOID(rs.getLong(fieldNames[FLD_ADDR_SUBREGENCY_ID]));
            entObj.setSubRegencyName(rs.getString(fieldNames[FLD_SUBREGENCY_NAME]));
            
            //Update 
            entObj.setIdRegency(rs.getLong(fieldNames[FLD_REGENCY_ID]));
            
        }catch(Exception e){
            System.out.println("resultToObject() appuser -> : " + e.toString());
        }
    }
    
}
