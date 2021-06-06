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
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author dw1p4
 */
public class PstJenisKredit extends DBHandler implements I_Language, I_DBInterface, I_DBType{
    public static final String TBL_KREDIT="aiso_type_kredit";
    public static final int FLD_TYPE_KREDIT_ID=0;
    public static final int FLD_NAME_KREDIT = 1;//Update tgl 15 Maret 2013 oleh Hadi
    public static final int FLD_MIN_KREDIT=2;
    public static final int FLD_MAX_KREDIT=3;
    public static final int FLD_BUNGA_MIN=4;
    public static final int FLD_BUNGA_MAX=5;
    public static final int FLD_BIAYA_ADMIN=6;
    public static final int FLD_PROVISI=7;
    public static final int FLD_DENDA=8;
    public static final int FLD_JANGKA_WAKTU_MIN=9;
    public static final int FLD_JANGKA_WAKTU_MAX=10;
    public static final int FLD_KEGUNAAN=11;
    public static final int FLD_TIPE_BUNGA=12;
    public static final int FLD_BERLAKU_MULAI=13;
    public static final int FLD_BERLAKU_SAMPAI=14;
    //update
    public static final int FLD_TIPE_DENDA_BERLAKU=15;
    public static final int FLD_TIPE_PERHITUNGAN_DENDA=16;
    public static final int FLD_FREKUENSI_DENDA=17;
    public static final int FLD_SATUAN_FREKUANSI_DENDA=18;
    public static final int FLD_TIPE_FREKUENSI_POKOK = 19;
    public static final int FLD_PERSENTASE_WAJIB = 20;
    public static final int FLD_DENDA_TOLERANSI = 21;
    public static final int FLD_NOMINAL_WAJIB = 22;
    //added by dewok 2018-05-22
    public static final int FLD_VARIABEL_DENDA = 23;
    //added by dewok 2018-05-22
    public static final int FLD_TIPE_VARIABEL_DENDA = 24;
    public static final int FLD_TIPE_FREKUENSI_DENDA = 25;
    
    public static final String [] fieldNames={
        "TYPE_KREDIT_ID",
        "NAME_KREDIT",//Update
        "MIN_KREDIT",
        "MAX_KREDIT",
        "BUNGA_MIN",
        "BUNGA_MAX",
        "BIAYA_ADMIN",
        "PROVISI",
        "DENDA",
        "JANGKA_WAKTU_MIN",
        "JANGKA_WAKTU_MAX",
        "KEGUNAAN",
        "TIPE_BUNGA",
        "BERLAKU_MULAI",
        "BERLAKU_SAMPAI",
        "TIPE_DENDA_BERLAKU",
        "TIPE_PERHITUNGAN_DENDA",
        "FREKUENSI_DENDA",
        "SATUAN_FREKUANSI_DENDA",
        "TIPE_FREKUENSI_POKOK",
        "PERSENTASE_WAJIB",
        "DENDA_TOLERANSI",
        "NOMINAL_WAJIB",
        "VARIABEL_DENDA",
        "TIPE_VARIABEL_DENDA",
        "TIPE_FREKUENSI_DENDA"
    };
    
    public static final int [] fieldTypes={
        TYPE_PK+TYPE_ID+TYPE_LONG,
        TYPE_STRING,//Update
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
    };
     
    public PstJenisKredit(){
    }
    
    public PstJenisKredit(int i) throws DBException {
        super(new PstJenisKredit());
    }
    
    
    public PstJenisKredit(String sOid) throws DBException {
        super(new PstJenisKredit(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstJenisKredit(long lOid) throws DBException {
        super(new PstJenisKredit(0));
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
        return TBL_KREDIT;
    }
    
    public String getPersistentName() {
        return new PstJenisKredit().getClass().getName();
    }
    
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    
    public long delete(Entity ent) throws Exception{
        return deleteExc(ent.getOID());
    }
    
    
    public long insert(Entity ent)throws Exception{
        try{
            return insert((JenisKredit) ent);
        } catch (Exception e){
            System.out.println(" EXC " + e);
            return 0;
        }
    }
    
    
    public long update(Entity ent) throws Exception{
        return update((JenisKredit) ent);
    }
    
    public long fetch(Entity ent) throws Exception{
        JenisKredit entObj = PstJenisKredit.fetch(ent.getOID());
        ent = (Entity)entObj;
        return ent.getOID();
    }
    
    public static JenisKredit fetch(long oid){
        JenisKredit entObj = new JenisKredit();
        try {
            PstJenisKredit pstObj = new PstJenisKredit(oid);
            entObj.setOID(oid);
            entObj.setNamaKredit(pstObj.getString(FLD_NAME_KREDIT));//Update
            entObj.setMinKredit(pstObj.getdouble(FLD_MIN_KREDIT));
            entObj.setMaxKredit(pstObj.getdouble(FLD_MAX_KREDIT));
            entObj.setBungaMin(pstObj.getdouble(FLD_BUNGA_MIN));
            entObj.setBungaMax(pstObj.getdouble(FLD_BUNGA_MAX));
            entObj.setBiayaAdmin(pstObj.getdouble(FLD_BIAYA_ADMIN));
            entObj.setProvisi(pstObj.getdouble(FLD_PROVISI));
            entObj.setDenda(pstObj.getdouble(FLD_DENDA));
            entObj.setJangkaWaktuMin(pstObj.getfloat(FLD_JANGKA_WAKTU_MIN));
            entObj.setJangkaWaktuMax(pstObj.getfloat(FLD_JANGKA_WAKTU_MAX));
            entObj.setKegunaan(pstObj.getString(FLD_KEGUNAAN));
            entObj.setTipeBunga(pstObj.getInt(FLD_TIPE_BUNGA));
            entObj.setBerlakuMulai(pstObj.getDate(FLD_BERLAKU_MULAI));
            entObj.setBerlakuSampai(pstObj.getDate(FLD_BERLAKU_SAMPAI));
            //update
            entObj.setTipeDendaBerlaku(pstObj.getInt(FLD_TIPE_DENDA_BERLAKU));
            entObj.setDendaToleransi(pstObj.getInt(FLD_DENDA_TOLERANSI));
            entObj.setTipePerhitunganDenda(pstObj.getInt(FLD_TIPE_PERHITUNGAN_DENDA));
            entObj.setFrekuensiDenda(pstObj.getInt(FLD_FREKUENSI_DENDA));
            entObj.setSatuanFrekuensiDenda(pstObj.getInt(FLD_SATUAN_FREKUANSI_DENDA));
            entObj.setTipeFrekuensiPokok(pstObj.getInt(FLD_TIPE_FREKUENSI_POKOK));
            entObj.setPersentaseWajib(pstObj.getdouble(FLD_PERSENTASE_WAJIB));
            entObj.setNominalWajib(pstObj.getdouble(FLD_NOMINAL_WAJIB));
            entObj.setVariabelDenda(pstObj.getInt(FLD_VARIABEL_DENDA));
            entObj.setTipeVariabelDenda(pstObj.getInt(FLD_TIPE_VARIABEL_DENDA));
            entObj.setTipeFrekuensiDenda(pstObj.getInt(FLD_TIPE_FREKUENSI_DENDA));
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
    public static long insert(JenisKredit entObj) throws DBException  {
     try{
        PstJenisKredit pstObj = new PstJenisKredit(0);
        
        pstObj.setString(FLD_NAME_KREDIT, entObj.getNamaKredit());//Update
        pstObj.setDouble(FLD_MIN_KREDIT, entObj.getMinKredit());
        pstObj.setDouble(FLD_MAX_KREDIT, entObj.getMaxKredit());
        pstObj.setDouble(FLD_BUNGA_MIN, entObj.getBungaMin());
        pstObj.setDouble(FLD_BUNGA_MAX, entObj.getBungaMax());
        pstObj.setDouble(FLD_BIAYA_ADMIN, entObj.getBiayaAdmin());
        pstObj.setDouble(FLD_PROVISI, entObj.getProvisi());
        pstObj.setDouble(FLD_DENDA, entObj.getDenda());
        pstObj.setFloat(FLD_JANGKA_WAKTU_MIN, entObj.getJangkaWaktuMin());
        pstObj.setFloat(FLD_JANGKA_WAKTU_MAX, entObj.getJangkaWaktuMax());
        pstObj.setString(FLD_KEGUNAAN, entObj.getKegunaan());
        pstObj.setInt(FLD_TIPE_BUNGA, entObj.getTipeBunga());
        pstObj.setDate(FLD_BERLAKU_MULAI, entObj.getBerlakuMulai());
        pstObj.setDate(FLD_BERLAKU_SAMPAI, entObj.getBerlakuSampai());
        //
        pstObj.setInt(FLD_TIPE_DENDA_BERLAKU, entObj.getTipeDendaBerlaku());
        pstObj.setInt(FLD_DENDA_TOLERANSI, entObj.getDendaToleransi());
        pstObj.setInt(FLD_TIPE_PERHITUNGAN_DENDA, entObj.getTipePerhitunganDenda());
        pstObj.setInt(FLD_FREKUENSI_DENDA, entObj.getFrekuensiDenda());
        pstObj.setInt(FLD_SATUAN_FREKUANSI_DENDA, entObj.getSatuanFrekuensiDenda());
        pstObj.setInt(FLD_TIPE_FREKUENSI_POKOK, entObj.getTipeFrekuensiPokok());
        pstObj.setDouble(FLD_PERSENTASE_WAJIB, entObj.getPersentaseWajib());
        pstObj.setDouble(FLD_NOMINAL_WAJIB, entObj.getNominalWajib());
        pstObj.setInt(FLD_VARIABEL_DENDA, entObj.getVariabelDenda());
        pstObj.setInt(FLD_TIPE_VARIABEL_DENDA, entObj.getTipeVariabelDenda());
        pstObj.setInt(FLD_TIPE_FREKUENSI_DENDA, entObj.getTipeFrekuensiDenda());
        pstObj.insert();
        entObj.setOID(pstObj.getlong(FLD_TYPE_KREDIT_ID));
        return entObj.getOID();
     }catch (DBException dbe){
         throw dbe;
     }catch(Exception e) {
        throw  new DBException(new PstJenisKredit(0), DBException.UNKNOWN);
     }  
    }
    
    public static long update(JenisKredit entObj) throws DBException{
        /**
         * update data pkl
         */
            try {
                PstJenisKredit pstObj = new PstJenisKredit(entObj.getOID());
                
                pstObj.setString(FLD_NAME_KREDIT, entObj.getNamaKredit());//Update
                pstObj.setDouble(FLD_MIN_KREDIT, entObj.getMinKredit());
                pstObj.setDouble(FLD_MAX_KREDIT, entObj.getMaxKredit());
                pstObj.setDouble(FLD_BUNGA_MIN, entObj.getBungaMin());
                pstObj.setDouble(FLD_BUNGA_MAX, entObj.getBungaMax());
                pstObj.setDouble(FLD_BIAYA_ADMIN, entObj.getBiayaAdmin());
                pstObj.setDouble(FLD_PROVISI, entObj.getProvisi());
                pstObj.setDouble(FLD_DENDA, entObj.getDenda());
                pstObj.setFloat(FLD_JANGKA_WAKTU_MIN, entObj.getJangkaWaktuMin());
                pstObj.setFloat(FLD_JANGKA_WAKTU_MAX, entObj.getJangkaWaktuMax());
                pstObj.setString(FLD_KEGUNAAN, entObj.getKegunaan());
                pstObj.setInt(FLD_TIPE_BUNGA, entObj.getTipeBunga());
                pstObj.setDate(FLD_BERLAKU_MULAI, entObj.getBerlakuMulai());
                pstObj.setDate(FLD_BERLAKU_SAMPAI, entObj.getBerlakuSampai());
                //
                pstObj.setInt(FLD_DENDA_TOLERANSI, entObj.getDendaToleransi());
                pstObj.setInt(FLD_TIPE_DENDA_BERLAKU, entObj.getTipeDendaBerlaku());
                pstObj.setInt(FLD_TIPE_PERHITUNGAN_DENDA, entObj.getTipePerhitunganDenda());
                pstObj.setInt(FLD_FREKUENSI_DENDA, entObj.getFrekuensiDenda());
                pstObj.setInt(FLD_SATUAN_FREKUANSI_DENDA, entObj.getSatuanFrekuensiDenda());
                pstObj.setInt(FLD_TIPE_FREKUENSI_POKOK, entObj.getTipeFrekuensiPokok());
                pstObj.setDouble(FLD_PERSENTASE_WAJIB, entObj.getPersentaseWajib());
                pstObj.setDouble(FLD_NOMINAL_WAJIB, entObj.getNominalWajib());
                pstObj.setInt(FLD_VARIABEL_DENDA, entObj.getVariabelDenda());
                pstObj.setInt(FLD_TIPE_VARIABEL_DENDA, entObj.getTipeVariabelDenda());
                pstObj.setInt(FLD_TIPE_FREKUENSI_DENDA, entObj.getTipeFrekuensiDenda());
                pstObj.update();
                return entObj.getOID();
            }catch (DBException dbe){
                
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
            PstJenisKredit pstObj = new PstJenisKredit(oid);
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
            String sql = " SELECT COUNT("+fieldNames[FLD_TYPE_KREDIT_ID] +") AS NRCOUNT" +
            " FROM " + TBL_KREDIT;
            
            
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
            String sql = "SELECT * FROM " + TBL_KREDIT + " ";
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
                JenisKredit kredit = new JenisKredit();
                resultToObject(rs, kredit);
                lists.add(kredit);
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
    
    private static void resultToObject(ResultSet rs, JenisKredit entObj) {
        try {
            entObj.setOID(rs.getLong(fieldNames[FLD_TYPE_KREDIT_ID]));
            entObj.setNamaKredit(rs.getString(fieldNames[FLD_NAME_KREDIT]));//Update
            entObj.setMinKredit(rs.getDouble(fieldNames[FLD_MIN_KREDIT]));
            entObj.setMaxKredit(rs.getDouble(fieldNames[FLD_MAX_KREDIT]));
            entObj.setBungaMin(rs.getDouble(fieldNames[FLD_BUNGA_MIN]));
            entObj.setBungaMax(rs.getDouble(fieldNames[FLD_BUNGA_MAX]));
            entObj.setBiayaAdmin(rs.getDouble(fieldNames[FLD_BIAYA_ADMIN]));
            entObj.setProvisi(rs.getDouble(fieldNames[FLD_PROVISI]));
            entObj.setDenda(rs.getDouble(fieldNames[FLD_DENDA]));
            entObj.setJangkaWaktuMin(rs.getFloat(fieldNames[FLD_JANGKA_WAKTU_MIN]));
            entObj.setJangkaWaktuMax(rs.getFloat(fieldNames[FLD_JANGKA_WAKTU_MAX]));
            entObj.setKegunaan(rs.getString(fieldNames[FLD_KEGUNAAN]));
            entObj.setTipeBunga(rs.getInt(fieldNames[FLD_TIPE_BUNGA]));
            entObj.setBerlakuMulai(rs.getDate(fieldNames[FLD_BERLAKU_MULAI]));
            entObj.setBerlakuSampai(rs.getDate(fieldNames[FLD_BERLAKU_SAMPAI]));
            //
            entObj.setDendaToleransi(rs.getInt(fieldNames[FLD_DENDA_TOLERANSI]));
            entObj.setTipeDendaBerlaku(rs.getInt(fieldNames[FLD_TIPE_DENDA_BERLAKU]));
            entObj.setTipePerhitunganDenda(rs.getInt(fieldNames[FLD_TIPE_PERHITUNGAN_DENDA]));
            entObj.setFrekuensiDenda(rs.getInt(fieldNames[FLD_FREKUENSI_DENDA]));
            entObj.setSatuanFrekuensiDenda(rs.getInt(fieldNames[FLD_SATUAN_FREKUANSI_DENDA]));
            entObj.setTipeFrekuensiPokok(rs.getInt(FLD_TIPE_FREKUENSI_POKOK));
            entObj.setPersentaseWajib(rs.getInt(FLD_PERSENTASE_WAJIB));
            entObj.setNominalWajib(rs.getInt(FLD_NOMINAL_WAJIB));
            entObj.setPersentaseWajib(rs.getDouble(FLD_PERSENTASE_WAJIB));
            entObj.setVariabelDenda(rs.getInt(FLD_VARIABEL_DENDA));
            entObj.setTipeVariabelDenda(rs.getInt(FLD_TIPE_VARIABEL_DENDA));
            entObj.setTipeFrekuensiDenda(rs.getInt(FLD_TIPE_FREKUENSI_DENDA));
        }catch(Exception e){
            System.out.println("resultToObject() appuser -> : " + e.toString());
        }
    }
    
}
