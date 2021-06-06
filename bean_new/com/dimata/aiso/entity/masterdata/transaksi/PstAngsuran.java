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
import com.dimata.qdep.entity.I_Persintent;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HaddyPuutraa
 */
public class PstAngsuran extends DBHandler implements I_Language, I_DBInterface, I_DBType, I_PersintentExc{
    public static final String TBL_ANGSURAN_NAME = "aiso_angsuran";
    
    public static final int FLD_ID_ANGSURAN = 0;
    public static final int FLD_ID_PINJAMAN = 1;
    public static final int FLD_TANGGAL_ANGSURAN = 2;
    public static final int FLD_JUMLAH_ANGSURAN = 3;
    
    public static final String[] fieldNames = {
        "ID_ANGSURAN",
        "ID_PINJAMAN",
        "TANGGAL_ANGSURAN",
        "JUMLAH_ANGSURAN"
    };
    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_ID + TYPE_PK,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_FLOAT
    };

    public PstAngsuran() {
    }
    
    public PstAngsuran(int i) throws DBException {
        super(new PstAngsuran());
    }
    
    
    public PstAngsuran(String sOid) throws DBException {
        super(new PstAngsuran(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstAngsuran(long lOid) throws DBException {
        super(new PstAngsuran(0));
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
        return TBL_ANGSURAN_NAME;
    }
    
    public String getPersistentName() {
        return new PstAngsuran().getClass().getName();
    }
    
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public static Angsuran fetchExc(long oid) throws DBException{ 
        Angsuran angsuran = new Angsuran();
        try{ 
            PstAngsuran pstAngsuran = new PstAngsuran(oid); 
            angsuran.setOID(oid);

            angsuran.setIdPinjaman(pstAngsuran.getlong(FLD_ID_PINJAMAN));
            angsuran.setTanggalAngsuran(pstAngsuran.getDate(FLD_TANGGAL_ANGSURAN));
            angsuran.setJumlahAngsuran(pstAngsuran.getdouble(FLD_JUMLAH_ANGSURAN));
                        
            return angsuran; 
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstAngsuran(0),DBException.UNKNOWN); 
	} 
    }
    
    public long fetchExc(Entity ent) throws Exception{ 
        Angsuran angsuran = fetchExc(ent.getOID()); 
	ent = (Entity)angsuran;
	return angsuran.getOID(); 
    }
    
    public static long insertExc(Angsuran angsuran) throws DBException{ 
	try{ 
            PstAngsuran pstAngsuran = new PstAngsuran(0);

            pstAngsuran.setLong(FLD_ID_PINJAMAN, angsuran.getIdPinjaman());
            pstAngsuran.setDate(FLD_TANGGAL_ANGSURAN, new Date());
            pstAngsuran.setFloat(FLD_JUMLAH_ANGSURAN, angsuran.getJumlahAngsuran());
                        
            pstAngsuran.insert(); 
            angsuran.setOID(pstAngsuran.getlong(FLD_ID_ANGSURAN));
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstAngsuran(0),DBException.UNKNOWN); 
	}
	return angsuran.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{ 
	return insertExc((Angsuran) ent); 
    }
    
    public static long updateExc(Angsuran angsuran) throws DBException{ 
        try{ 
            if(angsuran.getOID() != 0){ 
                PstAngsuran pstAngsuran = new PstAngsuran(angsuran.getOID());

                pstAngsuran.setLong(FLD_ID_PINJAMAN, angsuran.getIdPinjaman());
                //pstAngsuran.setDate(FLD_TANGGAL_ANGSURAN, new Date());
                pstAngsuran.setFloat(FLD_JUMLAH_ANGSURAN, angsuran.getJumlahAngsuran());
                    
                pstAngsuran.update(); 
                return angsuran.getOID();

            }
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstAngsuran(0),DBException.UNKNOWN); 
	}
	return 0;
    }
    
    public long updateExc(Entity ent) throws Exception{ 
        return updateExc((Angsuran) ent); 
    }
    
    public static long deleteExc(long oid) throws DBException{ 
        try{ 
            PstAngsuran pstAngsuran = new PstAngsuran(oid);
            pstAngsuran.delete();
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstAngsuran(0),DBException.UNKNOWN); 
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
            String sql = "SELECT COUNT(" + PstAngsuran.fieldNames[PstAngsuran.FLD_ID_ANGSURAN] + ") " + " FROM " + TBL_ANGSURAN_NAME;
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
            String sql = "SELECT * FROM "+ TBL_ANGSURAN_NAME;
            
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
                Angsuran angsuran = new Angsuran();
                resultToObject(rs, angsuran);
                lists.add(angsuran);
            }
            return lists;
        } catch (Exception e) {          
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    private static void resultToObject(ResultSet rs, Angsuran angsuran){
        try{
            angsuran.setOID(rs.getLong(PstAngsuran.fieldNames[PstAngsuran.FLD_ID_ANGSURAN]));
            angsuran.setIdPinjaman(rs.getLong(PstAngsuran.fieldNames[PstAngsuran.FLD_ID_PINJAMAN]));
            angsuran.setTanggalAngsuran(rs.getDate(PstAngsuran.fieldNames[PstAngsuran.FLD_TANGGAL_ANGSURAN]));
            angsuran.setJumlahAngsuran(rs.getDouble(PstAngsuran.fieldNames[PstAngsuran.FLD_JUMLAH_ANGSURAN]));
            
	}catch(Exception e){ }
    }
    
    public static double sumAngsuran(long idPinjaman) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            if (idPinjaman != 0) {
                String sql = "SELECT SUM("+fieldNames[FLD_JUMLAH_ANGSURAN]+") FROM "+ TBL_ANGSURAN_NAME+" "
                + " WHERE "+fieldNames[FLD_ID_PINJAMAN]+" = "+ idPinjaman;
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = (ResultSet) dbrs.getResultSet();
                double totalAngsuran = 0.0;
                while (rs.next()) {
                    totalAngsuran = rs.getInt(1);
                }
                return totalAngsuran;
            }
        } catch (Exception e) {          
            //System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return 0.0;
    }
}
