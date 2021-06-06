/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.transaksi;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HaddyPuutraa
 */
public class PstDeposito extends DBHandler implements I_Language, I_DBType, I_DBInterface, I_PersintentExc{
    public static final String TBL_DEPOSITO = "aiso_deposito";
    
    public static final int FLD_ID_DEPOSITO = 0;
    public static final int FLD_ID_ANGGOTA = 1;
    public static final int FLD_ID_JENIS_DEPOSITO = 2;
    public static final int FLD_ID_KELOMPOK = 3;
    public static final int FLD_TGL_PENGAJUAN_DEPOSITO = 4;
    public static final int FLD_JUMLAH_DEPOSITO = 5;
    public static final int FLD_STATUS = 6;
    
    public static final String[] fieldNames = {
        "ID_DEPOSITO",//0
        "ID_ANGGOTA",//1
        "ID_JENIS_DEPOSITO",//2
        "ID_KELOMPOK_KOPERASI",//3
        "TGL_PENGAJUAN_DEPOSITO",//4
        "JUMLAH_DEPOSITO",//5
        "STATUS"//6
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_ID + TYPE_PK,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_INT
    };
    
    public static final int DRAFT = 0;
    public static final int ACTIVE = 1;
    public static final int CLOSE = 2;
    
    public static final String[] statusDepositoKey = {"Draft","Aktif","Tutup"};
    
    public static final int[] statusDepositoValues = {0,1,2};
    
    public static Vector getStatusDepositoKey(){
    	Vector result = new Vector(1,1);
        for(int i=0;i<statusDepositoKey.length;i++){
        	result.add(statusDepositoKey[i]);
        }
        return result;
    }
    
    public static Vector getStatusDepositoValue(){
        Vector value = new Vector(1, 1);
        for(int i=0; i<statusDepositoValues.length; i++){
            value.add(Integer.toString(i));
        }
        return value;
    }
    
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
        return TBL_DEPOSITO;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstDeposito().getClass().getName();
    }

    public PstDeposito(){
        
    }
    
    public PstDeposito(int i) throws com.dimata.qdep.db.DBException{
        super(new PstDeposito());
    }
    
    public PstDeposito(String sOid) throws com.dimata.qdep.db.DBException { 
	super(new PstDeposito(0)); 
	if(!locate(sOid)){ 
            throw new com.dimata.qdep.db.DBException(this,com.dimata.qdep.db.DBException.RECORD_NOT_FOUND); 
        }else{ 
            return;
        }
    }

    public PstDeposito(long lOid) throws com.dimata.qdep.db.DBException { 
	super(new PstDeposito(0)); 
	String sOid = "0"; 
	try { 
            sOid = String.valueOf(lOid); 
	}catch(Exception e) { 
            throw new com.dimata.qdep.db.DBException(this,com.dimata.qdep.db.DBException.RECORD_NOT_FOUND); 
	} 
            
        if(!locate(sOid)){ 
            throw new com.dimata.qdep.db.DBException(this,com.dimata.qdep.db.DBException.RECORD_NOT_FOUND); 
        }else{ 
            return;        
        }
    }
    
    public static Deposito fetchExc(long oid) throws DBException{ 
        Deposito deposito = new Deposito();
        try{ 
            PstDeposito pstDeposito = new PstDeposito(oid); 
            deposito.setOID(oid);

            deposito.setIdAnggota(pstDeposito.getlong(FLD_ID_ANGGOTA));
            deposito.setIdJenisDeposito(pstDeposito.getlong(FLD_ID_JENIS_DEPOSITO));
            deposito.setIdKelompokKoperasi(pstDeposito.getlong(FLD_ID_KELOMPOK));
            deposito.setTanggalPengajuanDeposito(pstDeposito.getDate(FLD_TGL_PENGAJUAN_DEPOSITO));
            deposito.setJumlahDeposito(pstDeposito.getdouble(FLD_JUMLAH_DEPOSITO));
            deposito.setStatus(pstDeposito.getInt(FLD_STATUS));
                        
            return deposito; 
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstDeposito(0),DBException.UNKNOWN); 
	} 
    }
    
    public long fetchExc(Entity ent) throws Exception{ 
        Deposito deposito = fetchExc(ent.getOID()); 
	ent = (Entity)deposito;
	return deposito.getOID(); 
    }
    
    public static long insertExc(Deposito deposito) throws DBException{ 
	try{ 
            PstDeposito pstDeposito= new PstDeposito(0);

            pstDeposito.setLong(FLD_ID_ANGGOTA, deposito.getIdAnggota());
            pstDeposito.setLong(FLD_ID_JENIS_DEPOSITO, deposito.getIdJenisDeposito());
            pstDeposito.setLong(FLD_ID_KELOMPOK, deposito.getIdKelompokKoperasi());
            pstDeposito.setDate(FLD_TGL_PENGAJUAN_DEPOSITO, new Date());
            pstDeposito.setDouble(FLD_JUMLAH_DEPOSITO, deposito.getJumlahDeposito());
            pstDeposito.setInt(FLD_STATUS, deposito.getStatus());
                        
            pstDeposito.insert(); 
            deposito.setOID(pstDeposito.getlong(FLD_ID_ANGGOTA));
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstDeposito(0),DBException.UNKNOWN); 
	}
	return deposito.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{ 
	return insertExc((Deposito) ent); 
    }
    
    public static long updateExc(Deposito deposito) throws DBException{ 
        try{ 
            if(deposito.getOID() != 0){ 
                PstDeposito pstDeposito = new PstDeposito(deposito.getOID());

                pstDeposito.setLong(FLD_ID_ANGGOTA, deposito.getIdAnggota());
                pstDeposito.setLong(FLD_ID_JENIS_DEPOSITO, deposito.getIdJenisDeposito());
                pstDeposito.setLong(FLD_ID_KELOMPOK, deposito.getIdKelompokKoperasi());
                //pstDeposito.setDate(FLD_TGL_PENGAJUAN_DEPOSITO, deposito.getTanggalPengajuanDeposito());
                pstDeposito.setDouble(FLD_JUMLAH_DEPOSITO, deposito.getJumlahDeposito());
                pstDeposito.setInt(FLD_STATUS, deposito.getStatus());
                    
                pstDeposito.update(); 
                return deposito.getOID();

            }
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstDeposito(0),DBException.UNKNOWN); 
	}
	return 0;
    }
    
    public long updateExc(Entity ent) throws Exception{ 
        return updateExc((Deposito) ent); 
    }
    
    public static long deleteExc(long oid) throws DBException{ 
        try{ 
            PstDeposito pstDeposito = new PstDeposito(oid);
            pstDeposito.delete();
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstDeposito(0),DBException.UNKNOWN); 
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
            String sql = "SELECT COUNT(" + PstDeposito.fieldNames[PstDeposito.FLD_ID_DEPOSITO] + ") " + " FROM " + TBL_DEPOSITO;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            //System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();            
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
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
            String sql = "SELECT * FROM "+ TBL_DEPOSITO;
            
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
                Deposito deposito = new Deposito();
                resultToObject(rs, deposito);
                lists.add(deposito);
            }
            return lists;
        } catch (Exception e) {          
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    private static void resultToObject(ResultSet rs, Deposito deposito){
        try{
            deposito.setOID(rs.getLong(PstDeposito.fieldNames[PstDeposito.FLD_ID_DEPOSITO]));
            deposito.setIdAnggota(rs.getLong(PstDeposito.fieldNames[PstDeposito.FLD_ID_ANGGOTA]));
            deposito.setIdJenisDeposito(rs.getLong(PstDeposito.fieldNames[PstDeposito.FLD_ID_JENIS_DEPOSITO]));
            deposito.setIdKelompokKoperasi(rs.getLong(PstDeposito.fieldNames[PstDeposito.FLD_ID_KELOMPOK]));
            deposito.setTanggalPengajuanDeposito(rs.getDate(PstDeposito.fieldNames[PstDeposito.FLD_TGL_PENGAJUAN_DEPOSITO]));
            deposito.setJumlahDeposito(rs.getDouble(PstDeposito.fieldNames[PstDeposito.FLD_JUMLAH_DEPOSITO]));
            deposito.setStatus(rs.getInt(PstDeposito.fieldNames[PstDeposito.FLD_STATUS]));
            
	}catch(Exception e){ }
    }
}
