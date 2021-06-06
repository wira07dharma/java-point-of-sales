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
public class PstProvince extends DBHandler implements I_DBInterface, I_DBType, I_Persintent{
     public static final String TBL_PROVINCE="aiso_province";
    public static final int FLD_PROVINCE_ID=0;
    public static final int FLD_PROVINCE_NAME=1;
    
    public static final String [] fieldNames={
        "ADDR_PROVINCE_ID",
        "PROVINCE",
    };
    
    public static final int [] fieldTypes={
        TYPE_PK+TYPE_ID+TYPE_LONG,
        TYPE_STRING
    };
    
    public PstProvince(){
    }
    
    public PstProvince(int i) throws DBException {
        super(new PstProvince());
    }
    
    
    public PstProvince(String sOid) throws DBException {
        super(new PstProvince(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstProvince(long lOid) throws DBException {
        super(new PstProvince(0));
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
        return TBL_PROVINCE;
    }
    
    public String getPersistentName() {
        return new PstProvince().getClass().getName();
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
            return PstProvince.insert((Province) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    
    public long update(Entity ent) {
        return update((Province) ent);
    }
    
    public long fetch(Entity ent) {
        Province entObj = PstProvince.fetch(ent.getOID());
        ent = (Entity)entObj;
        return ent.getOID();
    }
    
    public static Province fetch(long oid) {
        Province entObj = new Province();
        try {
            PstProvince pstObj = new PstProvince(oid);
            entObj.setOID(oid);
            entObj.setProvinceName(pstObj.getString(FLD_PROVINCE_NAME));
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
    public static long insert(Province entObj) throws DBException  {
     try{
        PstProvince pstObj = new PstProvince(0);
        
        pstObj.setString(FLD_PROVINCE_NAME, entObj.getProvinceName());
        
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_PROVINCE_ID));
        return entObj.getOID();
     }catch(DBException e) {
        throw  e;
     }  
    }
    
    public static long update(Province entObj) {
        /**
         * update data pkl
         */
            try {
                PstProvince pstObj = new PstProvince(entObj.getOID());
                
                pstObj.setString(FLD_PROVINCE_NAME, entObj.getProvinceName());

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
            PstProvince pstObj = new PstProvince(oid);
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
            String sql = " SELECT COUNT("+fieldNames[FLD_PROVINCE_ID] +") AS NRCOUNT" +
            " FROM " + TBL_PROVINCE;
            
            
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
            String sql = "SELECT * FROM " + TBL_PROVINCE + " ";
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
                Province province = new Province();
                resultToObject(rs, province);
                lists.add(province);
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
    
    private static void resultToObject(ResultSet rs, Province entObj) {
        try {
            entObj.setOID(rs.getLong(fieldNames[FLD_PROVINCE_ID]));
            entObj.setProvinceName(rs.getString(fieldNames[FLD_PROVINCE_NAME]));
            
        }catch(Exception e){
            System.out.println("resultToObject() appuser -> : " + e.toString());
        }
    }
    
}
