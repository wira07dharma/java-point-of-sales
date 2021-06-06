/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.mastertabungan;

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
public class PstTabunganBerjangka extends DBHandler implements I_DBInterface, I_DBType, I_Persintent {
    public static final String TBL_TABUNGAN_BERJANGKA="aiso_tabungan_berjangka";
    public static final int FLD_TABUNGAN_BERJANGKA_ID=0;
    public static final int FLD_NAME=1;
    public static final int FLD_NILAI_TABUNGAN=2;
    public static final int FLD_PROSENTASE_NILAI=3;
    
    public static final String [] fieldNames={
        "TABUNGAN_BERJANGKA_ID",
        "NAME",
        "NILAI_TABUNGAN",
        "PROSENTASE_NILAI",
    };
    
    public static final int [] fieldTypes={
        TYPE_PK+TYPE_ID+TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT
    };
    
    public PstTabunganBerjangka(){
    }
    
    public PstTabunganBerjangka(int i) throws DBException {
        super(new PstTabunganBerjangka());
    }
    
    public PstTabunganBerjangka(String sOid) throws DBException {
        super(new PstTabunganBerjangka(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstTabunganBerjangka(long lOid) throws DBException {
        super(new PstTabunganBerjangka(0));
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
        return TBL_TABUNGAN_BERJANGKA;
    }
    
    public String getPersistentName() {
        return new PstTabunganBerjangka().getClass().getName();
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
            return PstTabunganBerjangka.insert((TabunganBerjangka) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    
    public long update(Entity ent) {
        return update((TabunganBerjangka) ent);
    }
    
    public long fetch(Entity ent) {
        TabunganBerjangka entObj = PstTabunganBerjangka.fetch(ent.getOID());
        ent = (Entity)entObj;
        return ent.getOID();
    }
    
    public static TabunganBerjangka fetch(long oid) {
        TabunganBerjangka entObj = new TabunganBerjangka();
        try {
            PstTabunganBerjangka pstObj = new PstTabunganBerjangka(oid);
            entObj.setOID(oid);
            entObj.setName(pstObj.getString(FLD_NAME));
            entObj.setNilaiTabungan(pstObj.getdouble(FLD_NILAI_TABUNGAN));
            entObj.setProsentaseNilai(pstObj.getdouble(FLD_PROSENTASE_NILAI));
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
    public static long insert(TabunganBerjangka entObj) throws DBException  {
     try{
        PstTabunganBerjangka pstObj = new PstTabunganBerjangka(0);
        
        pstObj.setString(FLD_NAME, entObj.getName());
        pstObj.setFloat(FLD_NILAI_TABUNGAN, entObj.getNilaiTabungan());
        pstObj.setFloat(FLD_PROSENTASE_NILAI, entObj.getProsentaseNilai());
        
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_TABUNGAN_BERJANGKA_ID));
        return entObj.getOID();
     }catch(DBException e) {
        throw  e;
     }  
    }
    
    public static long update(TabunganBerjangka entObj) {
        /**
         * update data pkl
         */
            try {
                PstTabunganBerjangka pstObj = new PstTabunganBerjangka(entObj.getOID());
                
                pstObj.setString(FLD_NAME, entObj.getName());
                pstObj.setFloat(FLD_NILAI_TABUNGAN, entObj.getNilaiTabungan());
                pstObj.setFloat(FLD_PROSENTASE_NILAI, entObj.getProsentaseNilai());

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
            PstTabunganBerjangka pstObj = new PstTabunganBerjangka(oid);
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
            String sql = " SELECT COUNT("+fieldNames[FLD_TABUNGAN_BERJANGKA_ID] +") AS NRCOUNT" +
            " FROM " + TBL_TABUNGAN_BERJANGKA;
            
            
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
            String sql = "SELECT * FROM " + TBL_TABUNGAN_BERJANGKA + " ";
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
                TabunganBerjangka tabunganBerjangka = new TabunganBerjangka();
                resultToObject(rs, tabunganBerjangka);
                lists.add(tabunganBerjangka);
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
    
    private static void resultToObject(ResultSet rs, TabunganBerjangka entObj) {
        try {
            entObj.setOID(rs.getLong(fieldNames[FLD_TABUNGAN_BERJANGKA_ID]));
            entObj.setName(rs.getString(fieldNames[FLD_NAME]));
            entObj.setNilaiTabungan(rs.getDouble(fieldNames[FLD_NILAI_TABUNGAN]));
            entObj.setProsentaseNilai(rs.getDouble(fieldNames[FLD_PROSENTASE_NILAI]));
            
        }catch(Exception e){
            System.out.println("resultToObject() appuser -> : " + e.toString());
        }
    }
    
}
