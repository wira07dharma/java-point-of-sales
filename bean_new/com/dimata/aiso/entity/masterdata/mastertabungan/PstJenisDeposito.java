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
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author HaddyPuutraa
 */
public class PstJenisDeposito extends DBHandler implements I_Language, I_DBType, I_DBInterface, I_PersintentExc{
    
    public static final String TBL_JENIS_DEPOSITO = "aiso_jenis_deposito";
    
    public static final int FLD_ID_JENIS_DEPOSITO = 0;
    public static final int FLD_NAMA_JENIS_DEPOSITO = 1;
    public static final int FLD_MIN_DEPOSITO = 2;
    public static final int FLD_MAX_DEPOSITO = 3;
    public static final int FLD_BUNGA = 4;
    public static final int FLD_JANGKA_WAKTU = 5;
    public static final int FLD_PROVISI = 6;
    public static final int FLD_BIAYA_ADMIN = 7;
    public static final int FLD_KETERANGAN = 8;
    
    public static final String[] fieldNames = {
        "ID_JENIS_DEPOSITO",//0
        "NAMA_JENIS_DEPOSITO",//1
        "MIN_DEPOSITO",//2
        "MAX_DEPOSITO",//3
        "BUNGA",//4
        "JANGKA_WAKTU",//5
        "PROVISI",//6
        "BIAYA_ADMIN",//7
        "KETERANGAN"//8
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_ID + TYPE_PK,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING
    };
    
    public int index;
    
    public int getIndex(){
        return this.index;
    }
    
    public void setIndex(int index){
        this.index = index;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_JENIS_DEPOSITO;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstJenisDeposito().getClass().getName();
    }

    public PstJenisDeposito() {
    }
    
    public PstJenisDeposito(int i) throws DBException{
        super(new PstJenisDeposito());
    }
    
    public PstJenisDeposito(String sOid) throws DBException { 
	super(new PstJenisDeposito(0)); 
	if(!locate(sOid)){ 
            throw new DBException(this,DBException.RECORD_NOT_FOUND); 
        }else{ 
            return;
        }
    }

    public PstJenisDeposito(long lOid) throws DBException { 
	super(new PstJenisDeposito(0)); 
	String sOid = "0"; 
	try { 
            sOid = String.valueOf(lOid); 
	}catch(Exception e) { 
            throw new DBException(this,DBException.RECORD_NOT_FOUND); 
	} 
            
        if(!locate(sOid)){ 
            throw new DBException(this,DBException.RECORD_NOT_FOUND); 
        }else{ 
            return;        
        }
    }
    
    public static JenisDeposito fetchExc(long oid) throws DBException{ 
        try{ 
            JenisDeposito jenisDeposito = new JenisDeposito();
            PstJenisDeposito pstJenisDeposito = new PstJenisDeposito(oid); 
            jenisDeposito.setOID(oid);

            jenisDeposito.setNamaJenisDeposito(pstJenisDeposito.getString(FLD_NAMA_JENIS_DEPOSITO));
            jenisDeposito.setMinDeposito(pstJenisDeposito.getdouble(FLD_MIN_DEPOSITO));
            jenisDeposito.setMaxDeposito(pstJenisDeposito.getdouble(FLD_MAX_DEPOSITO));
            jenisDeposito.setBunga(pstJenisDeposito.getdouble(FLD_BUNGA));
            jenisDeposito.setJangkaWaktu(pstJenisDeposito.getInt(FLD_JANGKA_WAKTU));
            jenisDeposito.setProvisi(pstJenisDeposito.getdouble(FLD_PROVISI));
            jenisDeposito.setBiayaAdmin(pstJenisDeposito.getdouble(FLD_BIAYA_ADMIN));
            jenisDeposito.setKeterangan(pstJenisDeposito.getString(FLD_KETERANGAN));
            
            return jenisDeposito; 
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstJenisDeposito(0),DBException.UNKNOWN); 
	} 
    }
    
    public long fetchExc(Entity ent) throws Exception{ 
        JenisDeposito jenisDeposito = fetchExc(ent.getOID()); 
	ent = (Entity)jenisDeposito; 
	return jenisDeposito.getOID(); 
    }
    
    public static long insertExc(JenisDeposito jenisDeposito) throws DBException{ 
	try{ 
            PstJenisDeposito pstJenisDeposito = new PstJenisDeposito(0);

            pstJenisDeposito.setString(FLD_NAMA_JENIS_DEPOSITO, jenisDeposito.getNamaJenisDeposito());
            pstJenisDeposito.setFloat(FLD_MIN_DEPOSITO, jenisDeposito.getMinDeposito());
            pstJenisDeposito.setFloat(FLD_MAX_DEPOSITO, jenisDeposito.getMaxDeposito());
            pstJenisDeposito.setFloat(FLD_BUNGA, jenisDeposito.getBunga());
            pstJenisDeposito.setInt(FLD_JANGKA_WAKTU, jenisDeposito.getJangkaWaktu());
            pstJenisDeposito.setFloat(FLD_PROVISI, jenisDeposito.getProvisi());
            pstJenisDeposito.setFloat(FLD_BIAYA_ADMIN, jenisDeposito.getBiayaAdmin());
            pstJenisDeposito.setString(FLD_KETERANGAN, jenisDeposito.getKeterangan());

            pstJenisDeposito.insert(); 
            jenisDeposito.setOID(pstJenisDeposito.getlong(FLD_ID_JENIS_DEPOSITO));
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstJenisDeposito(0),DBException.UNKNOWN); 
	}
	return jenisDeposito.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{ 
	return insertExc((JenisDeposito) ent); 
    }
    
    public static long updateExc(JenisDeposito jenisDeposito) throws DBException{ 
        try{ 
            if(jenisDeposito.getOID() != 0){ 
                PstJenisDeposito pstJenisDeposito = new PstJenisDeposito(jenisDeposito.getOID());

                pstJenisDeposito.setString(FLD_NAMA_JENIS_DEPOSITO, jenisDeposito.getNamaJenisDeposito());
                pstJenisDeposito.setFloat(FLD_MIN_DEPOSITO, jenisDeposito.getMinDeposito());
                pstJenisDeposito.setFloat(FLD_MAX_DEPOSITO, jenisDeposito.getMaxDeposito());
                pstJenisDeposito.setFloat(FLD_BUNGA, jenisDeposito.getBunga());
                pstJenisDeposito.setInt(FLD_JANGKA_WAKTU, jenisDeposito.getJangkaWaktu());
                pstJenisDeposito.setFloat(FLD_PROVISI, jenisDeposito.getProvisi());
                pstJenisDeposito.setFloat(FLD_BIAYA_ADMIN, jenisDeposito.getBiayaAdmin());
                pstJenisDeposito.setString(FLD_KETERANGAN, jenisDeposito.getKeterangan());

                pstJenisDeposito.update(); 
                return jenisDeposito.getOID();

            }
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstJenisDeposito(0),DBException.UNKNOWN); 
	}
	return 0;
    }
    
    public long updateExc(Entity ent) throws Exception{ 
        return updateExc((JenisDeposito) ent); 
    }
    
    public static long deleteExc(long oid) throws DBException{ 
        try{ 
            PstJenisDeposito pstJenisDeposito = new PstJenisDeposito(oid);
            pstJenisDeposito.delete();
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstJenisDeposito(0),DBException.UNKNOWN); 
	}
	return oid;
    }
    
    public long deleteExc(Entity ent) throws Exception{ 
        if(ent==null){ 
            throw new DBException(this,DBException.RECORD_NOT_FOUND); 
	} 
	return deleteExc(ent.getOID()); 
    }
    
    public static int getCount(String whereClause){
        DBResultSet dbrs = null;
	try {
            String sql = "SELECT COUNT("+ PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_ID_JENIS_DEPOSITO] + ") FROM " + TBL_JENIS_DEPOSITO;
            if(whereClause != null && whereClause.length() > 0){
                sql = sql + " WHERE " + whereClause;
            }
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
    
    public static Vector listAll(){
        return list(0, 0, "","");
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM "+ TBL_JENIS_DEPOSITO;
            
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
            com.mysql.jdbc.ResultSet rs = (com.mysql.jdbc.ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                JenisDeposito jenisDeposito = new JenisDeposito();
                resultToObject(rs, jenisDeposito);
                lists.add(jenisDeposito);
            }
            return lists;
        } catch (Exception e) {          
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    private static void resultToObject(ResultSet rs, JenisDeposito jenisDeposito){
        try{
            jenisDeposito.setOID(rs.getLong(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_ID_JENIS_DEPOSITO]));
            jenisDeposito.setNamaJenisDeposito(rs.getString(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_NAMA_JENIS_DEPOSITO]));
            jenisDeposito.setMinDeposito(rs.getDouble(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_MIN_DEPOSITO]));
            jenisDeposito.setMaxDeposito(rs.getDouble(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_MAX_DEPOSITO]));
            jenisDeposito.setBunga(rs.getDouble(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_BUNGA]));
            jenisDeposito.setJangkaWaktu(rs.getInt(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_JANGKA_WAKTU]));
            jenisDeposito.setProvisi(rs.getDouble(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_PROVISI]));
            jenisDeposito.setBiayaAdmin(rs.getDouble(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_BIAYA_ADMIN]));
            jenisDeposito.setKeterangan(rs.getString(PstJenisDeposito.fieldNames[PstJenisDeposito.FLD_KETERANGAN]));

	}catch(Exception e){ }
    }
}
